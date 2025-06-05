package com.icetea.MonStu.dto.request.auth;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record VerifyEmailCodeRequest(

        @NotNull
        @Min(value = 0, message = "{VerifyEmailCodeRequest.id.min.message}")
        Long id,

        @NotNull(message = "{VerifyEmailCodeRequest.code.NotNull}")
        String code
) {
}
