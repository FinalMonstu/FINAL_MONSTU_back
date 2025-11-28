package com.icetea.MonStu.auth.dto.v2.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmailRequest(
        @Email @NotBlank String email
) { }
