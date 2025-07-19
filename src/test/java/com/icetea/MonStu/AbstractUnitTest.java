package com.icetea.MonStu;

import com.icetea.MonStu.api.v1.infra.TranslationClient;
import com.icetea.MonStu.security.JwtAuthenticationFilter;
import com.icetea.MonStu.security.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


public abstract class AbstractUnitTest {

    @Autowired protected MockMvc mockMvc;
    @Autowired protected ObjectMapper objectMapper;

    // 컨트롤러가 의존하는 빈들을 모두 MockBean 으로 등록
    @MockitoBean protected TranslationClient translationClient;
    @MockitoBean protected com.google.cloud.translate.v3.TranslationServiceClient translationServiceClient;

    // 보안 필터, 서비스도 모킹 처리
    @MockitoBean protected JwtAuthenticationFilter jwtAuthenticationFilter;
    @MockitoBean protected JwtService jwtService;
}
