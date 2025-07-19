package com.icetea.MonStu.api.v1.dto.response.auth;

import com.icetea.MonStu.entity.VerifiCode;
import lombok.Builder;

@Builder
public record VerifiCodeResponse(
        Long id,
        String email,
        String message
) {

    public static VerifiCodeResponse toDto(VerifiCode vc) {
        return builder()
                .id(vc.getId())
                .email(vc.getEmail())
                .message("이메일을 확인해주세요.")
                .build();
    }
}
