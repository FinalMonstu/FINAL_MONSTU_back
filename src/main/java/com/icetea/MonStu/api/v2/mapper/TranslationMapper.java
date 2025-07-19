package com.icetea.MonStu.api.v2.mapper;

import com.icetea.MonStu.api.v2.dto.request.TranslationRequest;
import com.icetea.MonStu.api.v2.dto.response.TranslationResponse;

public final class TranslationMapper {

    private TranslationMapper() {}

    public static TranslationResponse toTranslationResponse(TranslationRequest req, String translatedText) {
        return TranslationResponse.builder()
                .originalText(req.getOriginalText())
                .translatedText( translatedText )
                .sourceLang(req.getSourceLang())
                .targetLang(req.getTargetLang())
                .textUnit(req.getTextUnit())
                .build();
    }

}
