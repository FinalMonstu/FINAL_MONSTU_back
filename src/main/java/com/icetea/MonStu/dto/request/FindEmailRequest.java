package com.icetea.MonStu.dto.request;

import com.icetea.MonStu.validation.ValidationConstants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record FindEmailRequest(
        @NotBlank
        @Pattern(regexp= ValidationConstants.PHONE_REGEX, message = "{Pattern.message.phonenumber}")
        String phoneNumber,

        @NotBlank
        String nickName
) { }
