package com.icetea.MonStu.member.repository;

import com.icetea.MonStu.auth.dto.v2.response.FindEmailResponse;
import com.icetea.MonStu.member.domain.Member;
import com.icetea.MonStu.member.enums.MemberStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member,Long>, QuerydslPredicateExecutor<Member>, MemberRepositoryCustom {

    boolean existsByEmail(String email);

    Optional<Member> findByEmail(String email);

    // Status가 'DELETE'인 모든 멤버 삭제
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM Member m WHERE m.status = :status")
    void deleteAllByStatus(MemberStatus status);

    Optional<FindEmailResponse> findByPhoneNumberAndNickName(String phoneNumber, String nickName);
}
