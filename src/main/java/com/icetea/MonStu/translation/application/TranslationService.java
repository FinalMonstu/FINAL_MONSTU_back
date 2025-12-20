package com.icetea.MonStu.translation.application;

import com.icetea.MonStu.shared.async.SemaphoreUtils;
import com.icetea.MonStu.translation.dto.v2.TranslationRequest;
import com.icetea.MonStu.translation.dto.v2.TranslationResponse;
import com.icetea.MonStu.translation.GoogleTranslateClient;
import com.icetea.MonStu.history.domain.History;
import com.icetea.MonStu.shared.common.enums.Genre;
import com.icetea.MonStu.shared.common.enums.LanguageCode;
import com.icetea.MonStu.history.repository.HistoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.*;


@Slf4j
@Service
public class TranslationService {

    private final HistoryRepository historyRps;
    private final GoogleTranslateClient translateClient;
    private final RedisTemplate<String, Object> redisTemplate;
    private final Semaphore translatePermits;
    private final AsyncTaskExecutor ioExecutor;

    private final ConcurrentMap<String, CompletableFuture<TranslationResponse>> inFlightRequests = new ConcurrentHashMap<>();

    private static final Duration CACHE_TTL = Duration.ofDays(1);

    public TranslationService(HistoryRepository historyRps,
                              GoogleTranslateClient translateClient,
                              RedisTemplate<String, Object> redisTemplate,
                              @Qualifier("translatePermits") Semaphore translatePermits,
                              @Qualifier("ioExecutor") AsyncTaskExecutor ioExecutor) {
        this.historyRps = historyRps;
        this.translateClient = translateClient;
        this.redisTemplate = redisTemplate;
        this.translatePermits = translatePermits;
        this.ioExecutor = ioExecutor;
    }

    public CompletableFuture<TranslationResponse> translateTextAsync(TranslationRequest request) {
        Genre genre = request.getGenre();
        String originalText = request.getOriginalText();
        LanguageCode sourceLang = request.getSourceLang();
        LanguageCode targetLang = request.getTargetLang();

        log.info("translateTextTerminal called - genre: {}, source: {}, target: {}, textLen: {}",
                genre, sourceLang, targetLang, originalText != null ? originalText.length() : 0);

        String redisKey = generateRedisKey(originalText, sourceLang, targetLang, genre);

        // 1) 캐시 확인
        Optional<TranslationResponse> cacheOpt = findFromRedis(redisKey);
        if (cacheOpt.isPresent()) {
            log.info("Redis Cache Hit! key: {}", redisKey);
            return CompletableFuture.completedFuture(cacheOpt.get());
        }

        // 2) DB 확인
        Optional<History> dbOpt = historyRps.findHistory(originalText, sourceLang, targetLang, genre);
        if (dbOpt.isPresent()) {
            History history = dbOpt.get();
            TranslationResponse response = TranslationMapper.toTranslationResponse(history);
            saveToRedis(redisKey, response); // DB 히트 시 캐시에 저장

            return CompletableFuture.completedFuture(response);
        }

        // 3) API 호출
        return getOrComputeFuture(request, redisKey);
    }

    // 중복 요청 방지
    private CompletableFuture<TranslationResponse> getOrComputeFuture(TranslationRequest request, String redisKey) {
        return inFlightRequests.computeIfAbsent(redisKey, key ->
                CompletableFuture.supplyAsync(() -> translateSaveAndCache(request, key), ioExecutor)
                        .orTimeout(10, TimeUnit.SECONDS)
                        .whenComplete((res, ex) -> inFlightRequests.remove(key))
        );
    }

    // 실제 API 호출 및 저장
    private TranslationResponse translateSaveAndCache(TranslationRequest request, String redisKey) {
        String translated = SemaphoreUtils.withPermit(() -> {
            return translateClient.translateText(
                    request.getOriginalText(),
                    request.getSourceLang(),
                    request.getTargetLang()
            );
        }, translatePermits);

        History toSave = TranslationMapper.toEntity(request, translated);
        History saved = historyRps.save(toSave);

        TranslationResponse response = TranslationMapper.toTranslationResponse(saved);

        saveToRedis(redisKey, response);

        return response;
    }

    private Optional<TranslationResponse> findFromRedis(String key) {
        try {
            // RedisTemplate 설정을 JSON으로 해뒀다면 자동으로 History 객체로 역직렬화됨
            Object cachedValue = redisTemplate.opsForValue().get(key);
            if (cachedValue instanceof TranslationResponse) {
                return Optional.of((TranslationResponse) cachedValue);
            }
        } catch (Exception e) {
            log.error("Redis get error", e);
        }
        return Optional.empty();
    }

    private void saveToRedis(String key, TranslationResponse history) {
        try {
            redisTemplate.opsForValue().set(key, history, CACHE_TTL);
        } catch (Exception e) {
            log.error("Redis set error", e);
        }
    }

    // 형식: "trans:src:tgt:genre:textHashCode"
    private String generateRedisKey(String text, LanguageCode src, LanguageCode tgt, Genre genre) {
        return String.format("trans:%s:%s:%s:%d", src, tgt, genre, text.hashCode());
    }

}
