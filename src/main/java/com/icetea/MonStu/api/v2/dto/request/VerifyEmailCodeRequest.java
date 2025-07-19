package com.icetea.MonStu.api.v2.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/* 이메일 인증 코드 요청 시 사용 */
public record VerifyEmailCodeRequest(

        @NotNull
        @Min(value = 0, message = "{VerifyEmailCodeRequest.id.min.message}")
        Long id,

        @NotNull(message = "{VerifyEmailCodeRequest.code.NotNull}")
        String code
) {
}
