package com.icetea.MonStu.dto.request;

import jakarta.validation.constraints.NotBlank;

public record FindEmailRequest(
        @NotBlank
        String phoneNumber,

        @NotBlank
        String nickName
) { }
