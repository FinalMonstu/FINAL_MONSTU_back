package com.icetea.MonStu.service;

import com.icetea.MonStu.api.v2.dto.request.TranslationRequest;
import com.icetea.MonStu.api.v2.dto.response.TranslationResponse;
import com.icetea.MonStu.api.v2.mapper.TranslationMapper;
import com.icetea.MonStu.client.GoogleTranslateClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TranslationService{

    private final GoogleTranslateClient translateClient;

    public TranslationResponse translateText(TranslationRequest req) {
        String translated = translateClient.translateText(req.getOriginalText(), req.getSourceLang(), req.getTargetLang());
        return TranslationMapper.toTranslationResponse(req, translated);
    }
}
