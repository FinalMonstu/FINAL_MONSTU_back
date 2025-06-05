package com.icetea.MonStu.controller;

import com.icetea.MonStu.AbstractIntegrationTest;
import com.icetea.MonStu.dto.request.auth.LoginRequest;
import com.icetea.MonStu.dto.response.auth.LiteMemberResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.icetea.MonStu.entity.Member;
import com.icetea.MonStu.enums.CountryCode;
import com.icetea.MonStu.enums.MemberRole;
import com.icetea.MonStu.enums.MemberStatus;
import com.icetea.MonStu.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Date;

public class AuthControllerIntegrationTest extends AbstractIntegrationTest {

    @Autowired private MemberRepository memberRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    private final String TEST_EMAIL = "test@example.com";
    private final String TEST_PASSWORD = "password123";

    @BeforeEach //매번 테스트 메서드가 실행되기 전에 호출
    void setUp() {
        memberRepository.deleteAll();

        Member user = Member.builder()
                .email(TEST_EMAIL)
                .password(passwordEncoder.encode(TEST_PASSWORD))
                .nickName("test_user")
                .phoneNumber("000-0000-0000")
                .createdAt(new Date())
                .updatedAt(new Date())
                .status(MemberStatus.ACTIVE)
                .role(MemberRole.MEMBER)
                .countryCode(CountryCode.KOR)
                .build();
        memberRepository.save(user);
    }

    /* --------------------------------- POST /api/auth/login <Success Scenario> --------------------------------- */
    @Test @DisplayName("POST /api/auth/login — 올바른 자격증명으로 200 OK, 사용자 정보 반환")
    void login_200_WhenCredentialsAreValid() throws Exception {
        LoginRequest request = new LoginRequest(TEST_EMAIL, TEST_PASSWORD);

        MvcResult mvcResult = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // 쿠키나 헤더에 JWT 토큰이 설정되어 있는지 검증 (예: "Authorization" 헤더)
                .andExpect(cookie().exists("jwtToken"))
                .andReturn();

        // Body 객체 검증
        LiteMemberResponse resp = objectMapper.readValue( mvcResult.getResponse().getContentAsString(), LiteMemberResponse.class);
        assertThat(resp).isNotNull();
        assertThat(resp).isInstanceOf(LiteMemberResponse.class);
        assertThat(resp.email()).isEqualTo(TEST_EMAIL);
    }

    /* --------------------------------- POST /api/auth/login <Failure Scenario> --------------------------------- */
    @Test
    @DisplayName("POST /api/auth/login — 잘못된 비밀번호로 400 Bad Request")
    void login_401_WhenPasswordIsInvalid() throws Exception {
        var request = new LoginRequest(TEST_EMAIL, "wrongPassword");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("POST /api/auth/login — 존재하지 않는 이메일로 400 Bad Request")
    void login_401_WhenEmailNotFound() throws Exception {
        var request = new LoginRequest("no-such@example.com", "irrelevant");
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }
}
