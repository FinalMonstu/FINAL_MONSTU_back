package com.icetea.MonStu.api.v1.dto.response.member;

import com.icetea.MonStu.entity.Member;
import com.icetea.MonStu.enums.CountryCode;
import com.icetea.MonStu.enums.MemberRole;
import com.icetea.MonStu.enums.MemberStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Deprecated
@Getter
@Setter
@Builder
@ToString
public class MemberResponse {
    private Long memberId;

    private String email;

    private String nickName;
    private String phoneNumber;

    private Date createdAt;
    private Date updatedAt;

    private MemberStatus status;
    private MemberRole role;
    private CountryCode countryCode;


    public static MemberResponse toDto(Member m) {
        return builder()
                .memberId(m.getId())
                .email(m.getEmail())
                .nickName(m.getNickName())
                .phoneNumber(m.getPhoneNumber())
                .createdAt(m.getCreatedAt())
                .updatedAt(m.getUpdatedAt())
                .status(m.getStatus())
                .role(m.getRole())
                .countryCode(m.getCountryCode())
                .build();
    }
}
