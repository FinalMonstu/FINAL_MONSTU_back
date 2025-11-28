package com.icetea.MonStu.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.translate.v3.TranslationServiceClient;
import com.icetea.MonStu.api.v1.controller.AiController;
import com.icetea.MonStu.api.v1.dto.request.TransRequest;
import com.icetea.MonStu.api.v1.infra.TranslationClient;
import com.icetea.MonStu.shared.security.jwt.JwtAuthenticationFilter;
import com.icetea.MonStu.shared.security.jwt.JwtService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AiController.class,
        excludeAutoConfiguration = {
                // 보안 자동설정 완전 제외
                org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
                org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration.class
        })
@AutoConfigureMockMvc(addFilters = false)  // MockMvc 레벨에서 필터 체인 비활성화
@ActiveProfiles("test")
class AiControllerValidationTest {

    @Autowired protected MockMvc mockMvc;
    @Autowired protected ObjectMapper objectMapper;

    @MockitoBean protected TranslationClient translationClient;
    @MockitoBean protected TranslationServiceClient translationServiceClient;

    @MockitoBean protected JwtAuthenticationFilter jwtAuthenticationFilter;
    @MockitoBean protected JwtService jwtService;

    /* ---------------------------------Failure Scenario--------------------------------- */
    @Test
    @DisplayName("POST /api/ai/trans — target이 blank, 400 Bad Request")
    void transTarget_Returns400_WhenTargetIsBlank() throws Exception {
        // given: target이 빈 문자열
        TransRequest badRequest = new TransRequest();
        badRequest.setTarget("");
        badRequest.setOriLang("English");
        badRequest.setTransLang("Korean");

        // when & then
        mockMvc.perform(post("/api/ai/trans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(badRequest)))
                .andExpect(status().isBadRequest());
    }
}

