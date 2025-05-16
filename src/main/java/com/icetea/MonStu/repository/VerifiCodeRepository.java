package com.icetea.MonStu.repository;

import com.icetea.MonStu.entity.VerifiCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface VerifiCodeRepository extends JpaRepository<VerifiCode,Long> {

    void deleteByExpiresAtBefore(LocalDateTime dateTime);

    void deleteByFailedCountGreaterThanEqualAndFailedAtBefore(Byte count, LocalDateTime dateTime);
}
