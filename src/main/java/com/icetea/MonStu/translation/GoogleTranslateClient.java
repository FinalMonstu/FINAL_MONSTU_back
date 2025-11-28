package com.icetea.MonStu.translation;

import com.google.cloud.translate.v3.*;
import com.icetea.MonStu.shared.common.enums.LanguageCode;
import com.icetea.MonStu.shared.exception.GoogleResourceExhaustedException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GoogleTranslateClient {

    private final TranslationServiceClient client;
    @Value("${gcp.project-id}") private String projectId;
    @Value("${gcp.location}") private String location;

    public String translateText(String text, LanguageCode sourceLang, LanguageCode targetLang) {
        LocationName parent = LocationName.of(projectId, location);
        TranslateTextRequest request = TranslateTextRequest.newBuilder()
                .setParent(parent.toString())
                .setMimeType("text/plain")
                .setTargetLanguageCode( targetLang.toString() )
                .addContents( text )
                .build();

        try {
            TranslateTextResponse response = client.translateText(request);
            return response.getTranslationsList()
                    .get(0)
                    .getTranslatedText();
        } catch (com.google.api.gax.rpc.ResourceExhaustedException e) {
            throw new GoogleResourceExhaustedException(null);
        }

//        TranslateTextResponse response = client.translateText(request);
//        return response.getTranslationsList().getFirst().getTranslatedText();
    }
}
