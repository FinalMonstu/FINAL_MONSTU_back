package com.icetea.MonStu.repository;

import com.icetea.MonStu.entity.Member;
import com.icetea.MonStu.enums.MemberStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member,Long>, QuerydslPredicateExecutor<Member>{

    boolean existsByEmail(String email);

    Optional<Member> findByEmail(String email);

    Optional<Member> findByPhoneNumberAndNickName(String phoneNumber, String nickName);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE Member m SET m.status = :status WHERE m.id IN :ids")
    void updateStatusByIds( @Param("ids") List<Long> ids,
                           @Param("status") MemberStatus status);

    // Status가 'DELETE'인 모든 멤버 삭제
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM Member m WHERE m.status = 'DELETED'")
    void deleteAllByStatus();

}
