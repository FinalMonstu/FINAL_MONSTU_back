package com.icetea.MonStu.dto.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.icetea.MonStu.entity.Member;
import com.icetea.MonStu.enums.CountryCode;
import com.icetea.MonStu.enums.MemberRole;
import com.icetea.MonStu.enums.MemberStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.util.StringUtils;

import java.util.Date;

@Getter
@Setter
@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MemberDTO {
    private Long memberId;

    private String email;

    private String nickName;
    private String phoneNumber;

    private Date createdAt;
    private Date updatedAt;

    private MemberStatus status;
    private MemberRole role;
    private CountryCode countryCode;


    public static MemberDTO mapper(Member e) {
        return builder()
                .memberId(e.getId())
                .email(e.getEmail())
                .nickName(e.getNickName())
                .phoneNumber(e.getPhoneNumber())
                .createdAt(e.getCreatedAt())
                .updatedAt(e.getUpdatedAt())
                .status(e.getStatus())
                .role(e.getRole())
                .countryCode(e.getCountryCode())
                .build();
    }
}
