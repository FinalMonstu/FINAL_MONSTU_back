package com.icetea.MonStu.member.dto.v2.request;

import com.icetea.MonStu.member.enums.CountryCode;
import com.icetea.MonStu.member.enums.MemberRole;
import com.icetea.MonStu.member.enums.MemberStatus;
import com.icetea.MonStu.shared.util.RegexPatterns;
import jakarta.validation.constraints.*;
import lombok.Builder;


@Builder
public record UpdateMemberRequest(

        @NotNull
        @Min(value = 0, message = "{Long.id.NotNull.Min.zero}")
        Long id,

        @Email @NotBlank String email,

        @NotBlank
        @Pattern(regexp= RegexPatterns.PHONE_REGEX, message = "{Pattern.message.phonenumber}")
        String phoneNumber,

        @NotBlank String nickName,

        @NotNull CountryCode country,

        @NotNull MemberRole role,

        @NotNull MemberStatus status

) implements MemberRequest { }
