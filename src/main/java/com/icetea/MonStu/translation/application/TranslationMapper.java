package com.icetea.MonStu.translation.application;

import com.icetea.MonStu.translation.dto.v2.TranslationRequest;
import com.icetea.MonStu.translation.dto.v2.TranslationResponse;
import com.icetea.MonStu.history.domain.History;

public final class TranslationMapper {

    private TranslationMapper() {}

    /*------------------------------------------------TranslationResponse-----------------------------------------*/

    public static TranslationResponse toTranslationResponse(History h) {
        return TranslationResponse.builder()
                .id             (h.getId())
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
