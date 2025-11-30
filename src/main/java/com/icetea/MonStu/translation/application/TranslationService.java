package com.icetea.MonStu.translation.application;

import com.icetea.MonStu.shared.async.SemaphoreUtils;
import com.icetea.MonStu.translation.dto.v2.TranslationRequest;
import com.icetea.MonStu.translation.dto.v2.TranslationResponse;
import com.icetea.MonStu.translation.GoogleTranslateClient;
import com.icetea.MonStu.history.domain.History;
import com.icetea.MonStu.shared.common.enums.Genre;
import com.icetea.MonStu.shared.common.enums.LanguageCode;
import com.icetea.MonStu.history.repository.HistoryRepository;
import com.icetea.MonStu.shared.cache.CustomTTLCircleCache;
import com.icetea.MonStu.shared.cache.objects.HistoryCacheKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.*;


@Slf4j
@Service
public class TranslationService {

    private final HistoryRepository historyRps;
    private final GoogleTranslateClient translateClient;
    private final CustomTTLCircleCache<HistoryCacheKey, History> historyCache;
    private final Semaphore translatePermits;
    private final AsyncTaskExecutor ioExecutor;

    private final ConcurrentMap<HistoryCacheKey, CompletableFuture<TranslationResponse>> inFlightRequests = new ConcurrentHashMap<>();


    public TranslationService(HistoryRepository historyRps,
                              GoogleTranslateClient translateClient,
                              CustomTTLCircleCache<HistoryCacheKey, History> historyCache,
                              @Qualifier("translatePermits") Semaphore translatePermits,
                              @Qualifier("ioExecutor") AsyncTaskExecutor ioExecutor) {
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

        // 3) API 호출
        try {
            return getOrComputeFuture(request, cacheKey).get(); // 결과 대기
        } catch (InterruptedException | ExecutionException e) {
            log.error("Translation task failed for key: {}", cacheKey, e);
            throw new RuntimeException("Translation failed", e);
        }
    }

    // 중복 요청 방지
    private CompletableFuture<TranslationResponse> getOrComputeFuture(TranslationRequest request, HistoryCacheKey cacheKey) {
        return inFlightRequests.computeIfAbsent(cacheKey, key ->
                CompletableFuture.supplyAsync(() -> translateSaveAndCache(request, key), ioExecutor)
                        .orTimeout(10, TimeUnit.SECONDS)
                        .whenComplete((res, ex) -> inFlightRequests.remove(key))
        );
    }

    // 실제 API 호출 및 저장
    private TranslationResponse translateSaveAndCache(TranslationRequest request, HistoryCacheKey cacheKey) {
        String translated = SemaphoreUtils.withPermit(() -> {

            return translateClient.translateText(
                    request.getOriginalText(),
                    request.getSourceLang(),
                    request.getTargetLang()
            );
        }, translatePermits);

        History toSave = TranslationMapper.toEntity(request, translated);
        History saved = historyRps.save(toSave);

        historyCache.putValue(cacheKey, saved);

        return TranslationMapper.toTranslationResponse(saved);
    }

    private Optional<History> findFromCache(HistoryCacheKey cacheKey) {
        return Optional.ofNullable(historyCache.getValue(cacheKey));
    }

    @Transactional(readOnly = true)
    protected Optional<History> findFromDB(String originalText, LanguageCode sourceLang, LanguageCode targetLang, Genre genre) {
        return historyRps.findExisting(originalText, sourceLang, targetLang, genre);
    }

}
