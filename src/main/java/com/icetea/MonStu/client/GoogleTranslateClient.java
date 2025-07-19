package com.icetea.MonStu.client;

import com.google.cloud.translate.v3.*;
import com.icetea.MonStu.enums.LanguageCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GoogleTranslateClient {

    private final TranslationServiceClient client;
    @Value("${gcp.project-id}") private String projectId;
    @Value("${gcp.location}") private String location;

    public String translateText(String text, String sourceLang, String targetLang) {
        LocationName parent = LocationName.of(projectId, location);
        TranslateTextRequest request = TranslateTextRequest.newBuilder()
                .setParent(parent.toString())
                .setMimeType("text/plain")
                .setTargetLanguageCode( LanguageCode.getCode(targetLang).toString() )
                .addContents( text )
//                .setSourceLanguageCode(sourceLang)
//                .setTargetLanguageCode(targetLang)
//                .addContents(text)
                .build();
        TranslateTextResponse response = client.translateText(request);
//        return response.getTranslationsList()
//                .stream()
//                .findFirst()
//                .map(Translation::getTranslatedText)
//                .orElseThrow(() ->
//                        new IllegalStateException("번역 결과가 없습니다."));
        return response.getTranslationsList().getFirst().getTranslatedText();
    }
}
