package com.icetea.MonStu.auth.dto.v2.request;

import com.icetea.MonStu.shared.util.RegexPatterns;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ResetPasswordRequest(
        @Email(message = "{Size.min.message}")
        @NotBlank(message = "{Size.min.message}")
        String email,

        @NotBlank
        @Size(min = 6, message = "{Size.min.message}")
        @Pattern(regexp = RegexPatterns.PASSWORD_REGEX, message = "{Pattern.message.password}")
        String password
) { }
