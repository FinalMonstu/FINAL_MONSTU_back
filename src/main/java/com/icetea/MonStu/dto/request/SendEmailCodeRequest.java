package com.icetea.MonStu.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SendEmailCodeRequest(

        Long id,

        @Email
        @NotBlank
        String email
) {}
