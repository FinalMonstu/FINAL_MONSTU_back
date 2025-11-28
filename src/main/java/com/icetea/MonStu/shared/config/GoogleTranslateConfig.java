package com.icetea.MonStu.shared.config;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.auth.Credentials;
import com.google.cloud.translate.v3.TranslationServiceClient;
import com.google.cloud.translate.v3.TranslationServiceSettings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
public class GoogleTranslateConfig {

    @Value("${gcp.credentials.location}")
    private Resource credentialsResource;

    @Bean
    public TranslationServiceClient translationServiceClient(
            @Value("${gcp.project-id}") String projectId,
            @Value("${gcp.location}") String location) throws Exception {

        // 1) JSON 키 로드
        Credentials creds = ServiceAccountCredentials.fromStream( credentialsResource.getInputStream() );

        // 2) Settings에 인증 정보 주입
        TranslationServiceSettings settings = TranslationServiceSettings.newBuilder()
                .setCredentialsProvider(FixedCredentialsProvider.create(creds))
                .build();

        // 3) client 생성
        return TranslationServiceClient.create(settings);
    }
}
