package com.icetea.MonStu.api.v1.dto.request.auth;

import com.icetea.MonStu.api.v1.dto.MemberRequest;
import com.icetea.MonStu.enums.CountryCode;
import com.icetea.MonStu.validation.ValidationConstants;
import jakarta.validation.constraints.*;
import lombok.Builder;

@Builder
public record  SignUpRequest(

        @Email @NotBlank String email,

        @NotBlank
        @Size(min = 6, message = "{Size.min.message}")
        @Pattern(regexp = ValidationConstants.PASSWORD_REGEX, message = "{Pattern.message.password}")
        String password,

        @NotBlank
        @Pattern(regexp= ValidationConstants.PHONE_REGEX, message = "{Pattern.message.phonenumber}")
        String phoneNumber,

        @NotBlank String nickName,

        @NotNull CountryCode country

) implements MemberRequest {}
