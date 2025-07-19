package com.icetea.MonStu.api.v2.dto.request;

import com.icetea.MonStu.api.v2.dto.MemberRequest;
import com.icetea.MonStu.enums.CountryCode;
import com.icetea.MonStu.enums.MemberRole;
import com.icetea.MonStu.enums.MemberStatus;
import com.icetea.MonStu.validation.ValidationConstants;
import jakarta.validation.constraints.*;
import lombok.Builder;

@Builder
public record CreateMemberRequest(

        @Email @NotBlank String email,

        @NotBlank
        @Size(min = 6, message = "{Size.min.message}")
        @Pattern(regexp = ValidationConstants.PASSWORD_REGEX, message = "{Pattern.message.password}")
        String password,

        @NotBlank String nickName,

        @NotNull CountryCode country,

        @NotBlank
        @Pattern(regexp= ValidationConstants.PHONE_REGEX, message = "{Pattern.message.phonenumber}")
        String phoneNumber,

        MemberStatus status,
        MemberRole role

) implements MemberRequest { }
