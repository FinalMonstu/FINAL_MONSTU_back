package com.icetea.MonStu.api.v1.infra;

import com.google.cloud.translate.v3.LocationName;
import com.google.cloud.translate.v3.TranslateTextRequest;
import com.google.cloud.translate.v3.TranslateTextResponse;
import com.google.cloud.translate.v3.TranslationServiceClient;
import com.icetea.MonStu.api.v1.dto.request.TransRequest;
import com.icetea.MonStu.enums.LanguageCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Deprecated
@Service
@RequiredArgsConstructor
public class TranslationClient {
    private final TranslationServiceClient client;

    @Value("${gcp.project-id}") private String projectId;

    @Value("${gcp.location}") private String location;


    public TransRequest translate(TransRequest transRequest){
        transRequest.setTransed( null );
        LocationName parent = LocationName.of(projectId, location);
        TranslateTextRequest request = TranslateTextRequest.newBuilder()
                .setParent(parent.toString())
                .setMimeType("text/plain")
                .setTargetLanguageCode( LanguageCode.getCode(transRequest.getTransLang()).toString() )
                .addContents( transRequest.getTarget() )
                .build();
        TranslateTextResponse response = client.translateText(request);
        String transed = response.getTranslationsList().getFirst().getTranslatedText();
        transRequest.setTransed(transed);
        return transRequest;
    }
}
