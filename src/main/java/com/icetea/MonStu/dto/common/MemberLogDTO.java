package com.icetea.MonStu.dto.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.icetea.MonStu.entity.log.MemberLog;
import com.icetea.MonStu.enums.CountryCode;
import com.icetea.MonStu.enums.MemberRole;
import com.icetea.MonStu.enums.MemberStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MemberLogDTO {
    //member information
    private Long memberId;
    private String email;
    private String nickName;
    private String phoneNumber;

    private Date createdAt;
    private Date updatedAt;

    private MemberStatus status;
    private MemberRole role;
    private CountryCode countryCode;

    //member_log information
    private Long memberLogId;
    private Byte failedLoginCount;
    private LocalDateTime failedLoginTime;
    private Date lastLogin;

    public static MemberLogDTO mapper(MemberLog e) {
        return builder()
                // Member 정보
                .memberId(e.getMember().getId())
                .email(e.getMember().getEmail())
                .nickName(e.getMember().getNickName())
                .phoneNumber(e.getMember().getPhoneNumber())
                .createdAt(e.getMember().getCreatedAt())
                .updatedAt(e.getMember().getUpdatedAt())
                .status(e.getMember().getStatus())
                .role(e.getMember().getRole())
                .countryCode(e.getMember().getCountryCode())
                // MemberLog 정보
                .memberLogId(e.getId())
                .failedLoginCount(e.getFailedLoginCount())
                .failedLoginTime(e.getFailedLoginTime())
                .lastLogin(e.getLastLogin())
                .build();
    }
}
