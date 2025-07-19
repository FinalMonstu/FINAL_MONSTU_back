package com.icetea.MonStu.api.v1.dto.request.auth;

import com.icetea.MonStu.validation.ValidationConstants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Deprecated
public record FindEmailRequest(
        @NotBlank
        @Pattern(regexp= ValidationConstants.PHONE_REGEX, message = "{Pattern.message.phonenumber}")
        String phoneNumber,

        @NotBlank String nickName
) { }
