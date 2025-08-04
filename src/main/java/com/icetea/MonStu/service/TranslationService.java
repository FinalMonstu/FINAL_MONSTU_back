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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TranslationService{

    private final HistoryRepository historyRps;

    private final GoogleTranslateClient translateClient;

    private final CustomTTLCircleCache<HistoryCacheKey, History> historyCache;

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

        // 캐시 조회
        History cache = findFromCache(cacheKey);
        if(cache!=null){
            System.out.println("번역 : 캐시 조회");
            return TranslationMapper.toTranslationResponse(cache);
        }

        // DB 조회
        Optional<History> historyOpt = findFromDB(originalText, sourceLang, targetLang, genre);
        if (historyOpt.isPresent()){
            History history = historyOpt.get();
            System.out.println("번역 : DB 조회");

            // DB에서 찾은 결과 캐시에 저장
            historyCache.putValue(cacheKey, history);

            return TranslationMapper.toTranslationResponse(request, history.getTranslatedText());
        }

        // 외부 번역 API 호출
        System.out.println("번역 : 외부 API 조회");
        History history = translateText(request);

        // 번역 결과 DB에 저장
        History savedHistory = historyRps.save(history);

        // 저장한 결과 캐시에 넣기
        historyCache.putValue(cacheKey, savedHistory);

        return TranslationMapper.toTranslationResponse(savedHistory);
    }

    protected History findFromCache(HistoryCacheKey cacheKey) {
        return historyCache.getValue(cacheKey);
    }


    private Optional<History> findFromDB(String originalText, LanguageCode sourceLang, LanguageCode targetLang, Genre genre) {
        return historyRps.findByOriginalTextAndSourceLangAndTargetLangAndGenre(originalText, sourceLang, targetLang, genre);
    }

    protected History translateText(TranslationRequest request) {
        String translated = translateClient.translateText(request.getOriginalText(), request.getSourceLang(), request.getTargetLang());
        return TranslationMapper.toEntity(request, translated);
    }
}
