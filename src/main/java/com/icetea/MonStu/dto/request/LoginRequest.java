package com.icetea.MonStu.dto.request;

import com.icetea.MonStu.validation.ValidationConstants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record LoginRequest(

        @Email
        @NotBlank
        String email,

        @NotBlank
        @Size(min = 6, message = "{Size.min.message}")
        @Pattern(regexp = ValidationConstants.PASSWORD_REGEX, message = "{Pattern.message.password}")
        String password
) {}
