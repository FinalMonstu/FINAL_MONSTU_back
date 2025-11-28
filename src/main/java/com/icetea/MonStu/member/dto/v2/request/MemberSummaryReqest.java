package com.icetea.MonStu.member.dto.v2.request;

import com.icetea.MonStu.member.domain.Member;
import com.icetea.MonStu.member.enums.CountryCode;
import com.icetea.MonStu.member.enums.MemberRole;
import com.icetea.MonStu.member.enums.MemberStatus;
import lombok.Builder;

@Builder
public record MemberSummaryReqest(
        Long id,
        String email,
        String nickName,
        MemberRole role,
        MemberStatus status,
        CountryCode countryCode
){
    public static MemberSummaryReqest toDto(Member m) {
        return MemberSummaryReqest.builder()
                .id(m.getId())
                .email(m.getEmail())
                .nickName(m.getNickName())
                .role(m.getRole())
                .status(m.getStatus())
                .countryCode(m.getCountryCode())
                .build();
    }
}
