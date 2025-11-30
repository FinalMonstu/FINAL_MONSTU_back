package com.icetea.MonStu.auth.dto.v2.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/* 이메일 인증 코드 DTO */
public record EmailVerifyRequest(

        Long id,

        @Email @NotBlank String email
) {
}
