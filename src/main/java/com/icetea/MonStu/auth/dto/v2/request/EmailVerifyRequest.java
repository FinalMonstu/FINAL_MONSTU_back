package com.icetea.MonStu.auth.dto.v2.request;

import com.icetea.MonStu.verification.domain.VerifiCode;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

/* 이메일 인증 코드 DTO */
public record EmailVerifyRequest(

        Long id,

        @Email @NotBlank String email
) {

    // id가 존재한다면 해당 row를 사용하도록 초기화
    public VerifiCode toEntity(String code) {
        VerifiCode.VerifiCodeBuilder v = VerifiCode.builder()
                .email      (email)
                .code       (code)
                .createdAt  (LocalDateTime.now())
                .expiresAt  (LocalDateTime.now().plusMinutes(3))
                .verified   (false)
                .failedCount((byte) 0);
        if (id != null) { v.id(id); }
        return v.build();
    }

}
