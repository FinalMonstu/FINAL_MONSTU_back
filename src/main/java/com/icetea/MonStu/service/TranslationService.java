package com.icetea.MonStu.service;

import com.icetea.MonStu.api.v2.dto.request.TranslationRequest;
import com.icetea.MonStu.api.v2.dto.response.TranslationResponse;
import com.icetea.MonStu.api.v2.mapper.TranslationMapper;
import com.icetea.MonStu.manager.SemaphoreManager;
import com.icetea.MonStu.client.GoogleTranslateClient;
import com.icetea.MonStu.entity.History;
import com.icetea.MonStu.enums.Genre;
import com.icetea.MonStu.enums.LanguageCode;
import com.icetea.MonStu.repository.HistoryRepository;
import com.icetea.MonStu.util.cache.CustomTTLCircleCache;
import com.icetea.MonStu.util.cache.objects.HistoryCacheKey;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import static com.icetea.MonStu.async.AsyncManager.firstNonEmpty;

@Service
public class TranslationService{

    private final HistoryRepository historyRps;

    private final GoogleTranslateClient translateClient;

    private final CustomTTLCircleCache<HistoryCacheKey, History> historyCache;

    private final Executor ioExecutor;
    private final Semaphore translatePermits;

    public TranslationService(HistoryRepository historyRps,
                              GoogleTranslateClient translateClient,
                              CustomTTLCircleCache<HistoryCacheKey, History> historyCache,
                              @Qualifier("ioExecutor") Executor ioExecutor,
                              @Qualifier("translatePermits") Semaphore translatePermits){
        this.historyRps = historyRps;
        this.translateClient = translateClient;
        this.historyCache = historyCache;
        this.ioExecutor = ioExecutor;
        this.translatePermits = translatePermits;
    }


    public TranslationResponse translateTextTerminal(TranslationRequest request){
        Genre genre             = request.getGenre();
        String originalText     = request.getOriginalText();
        LanguageCode sourceLang = request.getSourceLang();
        LanguageCode targetLang = request.getTargetLang();

        HistoryCacheKey cacheKey = HistoryCacheKey.builder()
                .originalText(originalText)
                .sourceLang(sourceLang)
                .targetLang(targetLang)
                .genre(genre)
                .build();

        // 1) 캐시/DB 레이스
        CompletableFuture<Optional<History>> cacheF = CompletableFuture
                .completedFuture(findFromCache(cacheKey))
                .completeOnTimeout(Optional.empty(), 2,TimeUnit.SECONDS);

        CompletableFuture<Optional<History>> dbF = CompletableFuture
                .supplyAsync(() -> findFromDB(originalText, sourceLang, targetLang, genre), ioExecutor)
                .completeOnTimeout(Optional.empty(), 2, TimeUnit.SECONDS)
                .exceptionally(ex -> Optional.empty())
                .whenComplete((opt, ex) -> opt.ifPresent(h -> historyCache.putValue(cacheKey, h)));

        // 2) 먼저 성공하는 작업 반환
        CompletableFuture<TranslationResponse> result =
                firstNonEmpty(cacheF, dbF)
                        .thenCompose(opt -> opt
                                .map(h -> CompletableFuture.completedFuture(
                                        TranslationMapper.toTranslationResponse(h)))
                                // 캐시/DB 조회 실패 시 외부 API 호출
                                .orElseGet(() ->
                                        CompletableFuture.supplyAsync(() ->
                                                        SemaphoreManager.withPermit(
                                                                () -> translateSaveAndCache(request, cacheKey),
                                                                translatePermits, 10, TimeUnit.SECONDS
                                                        ),
                                                ioExecutor
                                        ).orTimeout(10, TimeUnit.SECONDS)
                                )
                        );

        return result.join();
    }


    private TranslationResponse translateSaveAndCache(TranslationRequest request, HistoryCacheKey cacheKey) {
        String translated = translateClient.translateText( request.getOriginalText(), request.getSourceLang(), request.getTargetLang());
        History toSave = TranslationMapper.toEntity(request, translated);
        History saved  = historyRps.save(toSave);

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
