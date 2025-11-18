package com.icetea.MonStu.service;

import com.icetea.MonStu.api.v2.dto.request.TranslationRequest;
import com.icetea.MonStu.api.v2.dto.response.TranslationResponse;
import com.icetea.MonStu.api.v2.mapper.TranslationMapper;
import com.icetea.MonStu.client.GoogleTranslateClient;
import com.icetea.MonStu.entity.History;
import com.icetea.MonStu.enums.Genre;
import com.icetea.MonStu.enums.LanguageCode;
import com.icetea.MonStu.repository.HistoryRepository;
import com.icetea.MonStu.util.cache.CustomTTLCircleCache;
import com.icetea.MonStu.util.cache.objects.HistoryCacheKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.*;


@Slf4j
@Service
public class TranslationService {

    private final HistoryRepository historyRps;
    private final GoogleTranslateClient translateClient;
    private final CustomTTLCircleCache<HistoryCacheKey, History> historyCache;
    private final Semaphore translatePermits;
    private final ExecutorService ioExecutor;

    private final ConcurrentMap<HistoryCacheKey, CompletableFuture<TranslationResponse>> inFlightRequests = new ConcurrentHashMap<>();


    public TranslationService(HistoryRepository historyRps,
                              GoogleTranslateClient translateClient,
                              CustomTTLCircleCache<HistoryCacheKey, History> historyCache,
                              @Qualifier("translatePermits") Semaphore translatePermits,
                              @Qualifier("ioExecutor") ExecutorService ioExecutor) {
        this.historyRps = historyRps;
        this.translateClient = translateClient;
        this.historyCache = historyCache;
        this.translatePermits = translatePermits;
        this.ioExecutor = ioExecutor;
    }

    public TranslationResponse translateTextTerminal(TranslationRequest request) {
        Genre genre = request.getGenre();
        String originalText = request.getOriginalText();
        LanguageCode sourceLang = request.getSourceLang();
        LanguageCode targetLang = request.getTargetLang();

        log.info("translateTextTerminal called - genre: {}, source: {}, target: {}, textLen: {}",
                genre, sourceLang, targetLang, originalText != null ? originalText.length() : 0);

        HistoryCacheKey cacheKey = HistoryCacheKey.builder()
                .originalText(originalText)
                .sourceLang(sourceLang)
                .targetLang(targetLang)
                .genre(genre)
                .build();

        // 1) 캐시 확인
        Optional<History> cacheOpt = findFromCache(cacheKey);
        if (cacheOpt.isPresent()) {
            return TranslationMapper.toTranslationResponse(cacheOpt.get());
        }

        // 2) DB 확인
        Optional<History> dbOpt = findFromDB(originalText, sourceLang, targetLang, genre);
        if (dbOpt.isPresent()) {
            History history = dbOpt.get();
            historyCache.putValue(cacheKey, history); // DB 히트 시 캐시에 저장
            return TranslationMapper.toTranslationResponse(history);
        }

        // 3) "Key별 락"을 사용하여 API 호출 ---
        CompletableFuture<TranslationResponse> future = inFlightRequests.computeIfAbsent(cacheKey, key -> {

            return CompletableFuture.supplyAsync(() -> {
                        log.debug("Google API run. key: {}", key);
                        return translateSaveAndCache(request, key);
                    },ioExecutor)
                    .orTimeout(10, TimeUnit.SECONDS)
                    .whenComplete((response, ex) -> {
                        inFlightRequests.remove(key);
                    });
        });

        // 4) 결과 대기
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            log.error("Failed to get translation result from in-flight future: {}", e.getMessage(), e);
            throw new RuntimeException("Translation task failed", e);
        }
    }


    private TranslationResponse translateSaveAndCache(TranslationRequest request, HistoryCacheKey cacheKey) {
        String translated;
        try {
            translatePermits.acquire(); //세마포어 획득
            log.debug("Acquired API permit for key: {}", cacheKey);

            translated = translateClient.translateText(request.getOriginalText(), request.getSourceLang(), request.getTargetLang());

        } catch (InterruptedException e) {
            log.warn("API call interrupted while waiting for permit: {}", e.toString());
            Thread.currentThread().interrupt(); // 스레드 플래스 활성화-상위 메소드에 전달하기 위해 ( 예외에 의해 플래그가 켜지면 JVM이 자동으로 플래그 내림)
            throw new RuntimeException("Translation API permit acquisition interrupted", e);
        } finally {
            translatePermits.release(); // 세마포어 반드시 반납
            log.debug("Released API permit for key: {}", cacheKey);
        }

        History toSave = TranslationMapper.toEntity(request, translated);

        History saved = historyRps.save(toSave);
        historyCache.putValue(cacheKey, saved);
        return TranslationMapper.toTranslationResponse(saved);
    }

    private Optional<History> findFromCache(HistoryCacheKey cacheKey) {
        return Optional.ofNullable(historyCache.getValue(cacheKey));
    }

    private Optional<History> findFromDB(String originalText, LanguageCode sourceLang, LanguageCode targetLang, Genre genre) {
        return historyRps.findByOriginalTextAndSourceLangAndTargetLangAndGenre(originalText, sourceLang, targetLang, genre);
    }

}
