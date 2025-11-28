package com.icetea.MonStu.auth.dto.v2.response;

import com.icetea.MonStu.verification.domain.VerifiCode;
import lombok.Builder;

/*이메일 인증 시 응답으로 사용되는 DTO*/
@Builder
public record EmailVerifyResponse(
        Long id,
        String email,
        String message
) {

    public static EmailVerifyResponse toDto(VerifiCode vc) {
        return builder()
                .id         (vc.getId())
                .email      (vc.getEmail())
                .message    ("이메일을 확인해주세요.")
                .build();
    }
}
