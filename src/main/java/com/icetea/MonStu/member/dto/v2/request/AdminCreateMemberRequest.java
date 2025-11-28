package com.icetea.MonStu.member.dto.v2.request;

import com.icetea.MonStu.member.enums.CountryCode;
import com.icetea.MonStu.member.enums.MemberRole;
import com.icetea.MonStu.member.enums.MemberStatus;
import com.icetea.MonStu.shared.util.RegexPatterns;
import jakarta.validation.constraints.*;
import lombok.Builder;

@Builder
public record AdminCreateMemberRequest(

        @Email @NotBlank String email,

        @NotBlank
        @Size(min = 6, message = "{Size.min.message}")
        @Pattern(regexp = RegexPatterns.PASSWORD_REGEX, message = "{Pattern.message.password}")
        String password,

        @NotBlank String nickName,

        @NotNull CountryCode country,

        @NotBlank
        @Pattern(regexp= RegexPatterns.PHONE_REGEX, message = "{Pattern.message.phonenumber}")
        String phoneNumber,

        MemberStatus status,
        MemberRole role

) implements MemberRequest { }
