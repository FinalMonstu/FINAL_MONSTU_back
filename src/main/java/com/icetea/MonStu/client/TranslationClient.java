package com.icetea.MonStu.client;

import com.google.cloud.translate.v3.LocationName;
import com.google.cloud.translate.v3.TranslateTextRequest;
import com.google.cloud.translate.v3.TranslateTextResponse;
import com.google.cloud.translate.v3.TranslationServiceClient;
import com.icetea.MonStu.dto.request.TransDTO;
import com.icetea.MonStu.enums.LanguageCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TranslationClient {
    private final TranslationServiceClient client;

    @Value("${gcp.project-id}")
    private String projectId;

    @Value("${gcp.location}")
    private String location;


    public TransDTO translate(TransDTO transDTO){
        transDTO.setTransed( null );
        System.out.println("Translate API Working: "+ transDTO.getTarget());
        LocationName parent = LocationName.of(projectId, location);
        TranslateTextRequest request = TranslateTextRequest.newBuilder()
                .setParent(parent.toString())
                .setMimeType("text/plain")
                .setTargetLanguageCode( LanguageCode.getCode(transDTO.getTransLang()).toString() )
                .addContents( transDTO.getTarget() )
                .build();
        TranslateTextResponse response = client.translateText(request);
        String transed = response.getTranslationsList().getFirst().getTranslatedText();
        transDTO.setTransed(transed);
        return transDTO;
    }
}
