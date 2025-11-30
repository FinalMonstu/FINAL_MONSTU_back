package com.icetea.MonStu.verification.repository;

import com.icetea.MonStu.verification.domain.VerifiCode;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface VerifiCodeRepository extends JpaRepository<VerifiCode,Long> {

    @Modifying
    @Query("DELETE FROM VerifiCode v WHERE v.expiresAt < :now")
    void deleteByExpiresAtBefore(LocalDateTime dateTime);

    void deleteByFailedCountGreaterThanEqualAndFailedAtBefore(Byte count, LocalDateTime dateTime);

    Optional<VerifiCode> findByEmail(String email);
}
