package com.icetea.MonStu.dto.request;

import com.icetea.MonStu.dto.MemberRequest;
import com.icetea.MonStu.enums.CountryCode;
import com.icetea.MonStu.enums.MemberRole;
import com.icetea.MonStu.enums.MemberStatus;
import com.icetea.MonStu.validation.ValidationConstants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record SaveMemberRequest (

        @Email
        @NotBlank
        String email,

        @NotBlank
        @Size(min = 6, message = "{Size.min.message}")
        @Pattern(regexp = ValidationConstants.PASSWORD_REGEX, message = "{Pattern.message.password}")
        String password,

        @NotBlank
        String nickName,

        @NotBlank
        CountryCode country,

        @NotBlank
        @Pattern(regexp= ValidationConstants.PHONE_REGEX, message = "{Pattern.message.phonenumber}")
        String phoneNumber,

        /* 회원가입 시 -> service에서 기본값 추가
        *  어드민 유저 추가 시 -> 요청에서 캐스팅 */
        MemberStatus status,
        MemberRole role

) implements MemberRequest { }
