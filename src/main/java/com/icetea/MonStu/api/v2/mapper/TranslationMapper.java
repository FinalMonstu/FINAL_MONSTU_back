package com.icetea.MonStu.api.v2.mapper;

import com.icetea.MonStu.api.v2.dto.request.TranslationRequest;
import com.icetea.MonStu.api.v2.dto.response.TranslationResponse;
import com.icetea.MonStu.entity.History;

public final class TranslationMapper {

    private TranslationMapper() {}

    /*------------------------------------------------TranslationResponse-----------------------------------------*/
    public static TranslationResponse toTranslationResponse(TranslationRequest req, String translatedText) {
        return TranslationResponse.builder()
                .originalText(req.getOriginalText())
                .translatedText( translatedText )
                .sourceLang(req.getSourceLang())
                .targetLang(req.getTargetLang())
                .genre(req.getGenre())
                .build();
    }

    public static TranslationResponse toTranslationResponse(History h) {
        return TranslationResponse.builder()
                .originalText   (h.getOriginalText())
                .translatedText (h.getTranslatedText() )
                .sourceLang     (h.getSourceLang())
                .targetLang     (h.getTargetLang())
                .genre          (h.getGenre())
                .build();
    }

    /*------------------------------------------------[entity] History-----------------------------------------*/
    // TranslationRequest → History
    public static History toEntity(TranslationRequest request, String translatedText) {
        return History.builder()
                .originalText   (request.getOriginalText())
                .sourceLang     (request.getSourceLang())
                .targetLang     (request.getTargetLang())
                .genre          (request.getGenre())
                .translatedText (translatedText)
                .build();
    }
}
