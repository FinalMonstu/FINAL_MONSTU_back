package com.icetea.MonStu.api.v2.dto.response;

import com.icetea.MonStu.entity.Member;
import com.icetea.MonStu.enums.CountryCode;
import com.icetea.MonStu.enums.MemberRole;
import com.icetea.MonStu.enums.MemberStatus;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MemberSummaryResponse {
    private Long memberId;

    private String email;

    private String nickName;
    private String phoneNumber;

    private LocalDate createdAt;
    private LocalDate updatedAt;

    private MemberStatus status;
    private MemberRole role;
    private CountryCode countryCode;


    public static MemberSummaryResponse toDto(Member m) {
        return builder()
                .memberId       (m.getId())
                .email          (m.getEmail())
                .nickName       (m.getNickName())
                .phoneNumber    (m.getPhoneNumber())
                .createdAt      (m.getCreatedAt())
                .updatedAt      (m.getUpdatedAt())
                .status         (m.getStatus())
                .role           (m.getRole())
                .countryCode    (m.getCountryCode())
                .build();
    }
}
