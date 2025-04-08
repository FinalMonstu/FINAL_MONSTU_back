package com.icetea.MonStu.repository;

import com.icetea.MonStu.entity.VerifiCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface VerifiCodeRepository extends JpaRepository<VerifiCode,Long> {

    boolean existsByEmail(String email);

    Optional<VerifiCode> findByIdAndCode(Long id, String code);

    Optional<VerifiCode> findByIdAndEmailAndCodeAndExpiresAtGreaterThanEqual(Long id, String email, String code, LocalDateTime now);

    void deleteByVerifiedTrueAndExpiresAtBefore(LocalDateTime dateTime);

    void deleteByFailedCountGreaterThanEqualAndFailedAtBefore(Byte count, LocalDateTime dateTime);
}
