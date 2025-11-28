package com.icetea.MonStu.auth.dto.v2.request;

import com.icetea.MonStu.shared.util.RegexPatterns;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record FindEmailRequest(
        @NotBlank
        @Pattern(regexp= RegexPatterns.PHONE_REGEX, message = "{Pattern.message.phonenumber}")
        String phoneNumber,

        @NotBlank String nickName
) { }
