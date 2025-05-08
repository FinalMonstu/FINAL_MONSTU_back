package com.icetea.MonStu.dto.request;

import com.icetea.MonStu.dto.MemberRequest;
import com.icetea.MonStu.entity.Member;
import com.icetea.MonStu.enums.CountryCode;
import com.icetea.MonStu.enums.MemberRole;
import com.icetea.MonStu.enums.MemberStatus;
import com.icetea.MonStu.validation.ValidationConstants;
import jakarta.validation.constraints.*;
import lombok.Builder;

import java.util.Date;

@Builder
public record UpdateMemberRequest(

        @NotNull
        @Min(value = 0, message = "{Long.id.NotNull.Min.zero}")
        Long id,

        @Email
        @NotBlank
        String email,

        @NotBlank
        @Pattern(regexp= ValidationConstants.PHONE_REGEX, message = "{Pattern.message.phonenumber}")
        String phoneNumber,

        @NotBlank
        String nickName,

        @NotNull
        CountryCode country,

        @NotNull
        MemberRole role,

        @NotNull
        MemberStatus status

) implements MemberRequest {

}
