package com.icetea.MonStu.repository;

import com.icetea.MonStu.entity.Member;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member,Long> {

    boolean existsByEmail(String email);

    Optional<Member> findByEmail(String email);

    @Modifying
    @Query("UPDATE Member m SET m.password = :password WHERE m.email = :email")
    int updatePasswordByEmail(@Param("email") String email, @Param("password") String password);

    Optional<Member> findByPhoneNumberAndNickName(String phoneNumber, String nickName);
}
