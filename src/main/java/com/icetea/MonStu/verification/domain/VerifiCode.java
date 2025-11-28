package com.icetea.MonStu.verification.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="verifi_code")
public class VerifiCode {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String code;

    @Column
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime expiresAt;

    @Column
    private Boolean verified;

    @Column
    private Byte failedCount;     // -128 ~ 127

    @Column
    private LocalDateTime failedAt;


    // 유효기간 검증
    public boolean isExpired() {
        return expiresAt.isBefore(LocalDateTime.now());
    }

    // code 검증
    public boolean matches(String inputCode) {
        return this.code.equals(inputCode);
    }

    // 유효기간 & code 검증
    public boolean verify(String inputCode) {
        if (matches(inputCode) && !isExpired()) {
            this.verified = true;
            return true;
        } else {
            incrementFailedAttempt();
            this.failedAt = LocalDateTime.now();
            return false;
        }
    }

    // 인증 실패 시, 실패 횟수 증가 & 실패 시간 기록
    public void incrementFailedAttempt() {
        this.failedCount = (byte)((failedCount == null ? 0 : failedCount) + 1);
        this.failedAt    = LocalDateTime.now();
    }
}
