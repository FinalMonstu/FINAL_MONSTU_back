package com.icetea.MonStu.controller;

import com.icetea.MonStu.AbstractIntegrationTest;
import com.icetea.MonStu.api.v1.dto.request.TransRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;


import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class AiControllerIntegrationTest extends AbstractIntegrationTest {

    /* --------------------------------- POST /api/ai/trans <Success Scenario> --------------------------------- */
    @Test @DisplayName("POST /api/ai/trans — 번역 성공 시 200 반환")
    void transTarget_success() throws Exception {
        // given
        TransRequest req = new TransRequest();
        req.setTarget("hello");
        req.setOriLang("English");
        req.setTransLang("Korean");

        TransRequest resp = new TransRequest();
        resp.setTarget("hello");
        resp.setOriLang("English");
        resp.setTransLang("Korean");
        resp.setTransed("안녕하세요");

        given(translationClient.translate(ArgumentMatchers.any(TransRequest.class)))
                .willReturn(resp);

        // when / then
        mockMvc.perform(post("/api/ai/trans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transed").value("안녕하세요"))
                .andExpect(jsonPath("$.target").value("hello"));
    }

    /* --------------------------------- POST /api/ai/trans <Failure Scenario> --------------------------------- */
    @Test @DisplayName("POST /api/ai/trans — 번역 실패 시 500 반환")
    void transTarget_500_failure() throws Exception {
        // given
        TransRequest req = new TransRequest();
        req.setTarget("hello");
        req.setOriLang("English");
        req.setTransLang("Korean");

        TransRequest resp = new TransRequest();
        resp.setTarget("hello");
        resp.setOriLang("English");
        resp.setTransLang("Korean");
        resp.setTransed(null);

        given(translationClient.translate(ArgumentMatchers.any(TransRequest.class)))
                .willReturn(resp);

        // when / then
        mockMvc.perform(post("/api/ai/trans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isInternalServerError());
    }

}
