package com.icetea.MonStu;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.translate.v3.TranslationServiceClient;
import com.icetea.MonStu.api.v1.infra.TranslationClient;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(MySQLTestContainerExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public abstract class AbstractIntegrationTest {

    @Autowired protected MockMvc mockMvc;
    @Autowired  protected ObjectMapper objectMapper;

    @MockitoBean protected TranslationClient translationClient;
    @MockitoBean protected TranslationServiceClient translationServiceClient;

}
