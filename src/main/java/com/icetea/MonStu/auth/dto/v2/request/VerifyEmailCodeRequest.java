package com.icetea.MonStu.auth.dto.v2.request;

import jakarta.validation.constraints.NotNull;

/* 이메일 인증 코드 요청 시 사용 */
public record VerifyEmailCodeRequest(

        @NotNull(message = "{VerifyEmailCodeRequest.email.NotNull}")
        String email,

        @NotNull(message = "{VerifyEmailCodeRequest.code.NotNull}")
        String code
) {
}
