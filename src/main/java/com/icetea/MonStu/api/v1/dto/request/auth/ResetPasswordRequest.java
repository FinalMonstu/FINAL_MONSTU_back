package com.icetea.MonStu.api.v1.dto.request.auth;

import com.icetea.MonStu.validation.ValidationConstants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Deprecated
public record ResetPasswordRequest(
        @Email(message = "{Size.min.message}")
        @NotBlank(message = "{Size.min.message}")
        String email,

        @NotBlank
        @Size(min = 6, message = "{Size.min.message}")
        @Pattern(regexp = ValidationConstants.PASSWORD_REGEX, message = "{Pattern.message.password}")
        String password
) { }
