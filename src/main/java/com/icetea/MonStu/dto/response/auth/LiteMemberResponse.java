package com.icetea.MonStu.dto.response.auth;

import com.icetea.MonStu.entity.Member;
import com.icetea.MonStu.enums.CountryCode;
import com.icetea.MonStu.enums.MemberRole;
import com.icetea.MonStu.enums.MemberStatus;
import lombok.*;

@Builder
public record LiteMemberResponse(
        Long id,
        String email,
        String nickName,
        MemberRole role,
        MemberStatus status,
        CountryCode countryCode
){
    public static LiteMemberResponse toDto(Member m) {
        return LiteMemberResponse.builder()
                .id(m.getId())
                .email(m.getEmail())
                .nickName(m.getNickName())
                .role(m.getRole())
                .status(m.getStatus())
                .countryCode(m.getCountryCode())
                .build();
    }
}
