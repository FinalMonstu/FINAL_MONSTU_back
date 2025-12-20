package com.icetea.MonStu.verification.repository;

import com.icetea.MonStu.verification.domain.VerifiCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface VerifiCodeRepository extends JpaRepository<VerifiCode,Long> {

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM VerifiCode v WHERE v.expiresAt < :now")
    void deleteByExpiresAtBefore(@Param("now") LocalDateTime now);


    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM VerifiCode v WHERE v.failedCount >= :count AND v.failedAt < :dateTime")
    void deleteByFailedCountAndDate(@Param("count") Byte count, @Param("dateTime") LocalDateTime dateTime);

    Optional<VerifiCode> findByEmail(String email);
}
