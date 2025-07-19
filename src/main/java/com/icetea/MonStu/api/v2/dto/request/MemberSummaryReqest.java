package com.icetea.MonStu.api.v2.dto.request;

import com.icetea.MonStu.entity.Member;
import com.icetea.MonStu.enums.CountryCode;
import com.icetea.MonStu.enums.MemberRole;
import com.icetea.MonStu.enums.MemberStatus;
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
