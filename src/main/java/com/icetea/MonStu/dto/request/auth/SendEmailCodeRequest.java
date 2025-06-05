package com.icetea.MonStu.dto.request.auth;

import com.icetea.MonStu.entity.VerifiCode;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public record SendEmailCodeRequest(

        Long id,

        @Email @NotBlank String email
) {

    public VerifiCode toEntity(String code) {
        VerifiCode.VerifiCodeBuilder v = VerifiCode.builder()
                .email(email)
                .code(code)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(3))
                .verified(false)
                .failedCount((byte) 0);
        if (id != null) { v.id(id); }
        return v.build();
    }

}
