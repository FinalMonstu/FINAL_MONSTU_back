package com.icetea.MonStu.member.repository;

import com.icetea.MonStu.member.domain.MemberLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberLogRepository extends JpaRepository<MemberLog,Long>, QuerydslPredicateExecutor<MemberLog>, MemberLogRepositoryCustom {

    // Member의 Status가 DELETE인 요소의 member_id를 null로 변경
//    @Modifying(clearAutomatically = true, flushAutomatically = true)
//    @Transactional
//    @Query("""
//        UPDATE MemberLog ml
//        SET ml.member = NULL
//        WHERE ml.member.id IN (
//          SELECT m.id FROM Member m WHERE m.status = :status
//        ) """)
//    int nullifyLogsByMemberStatus(@Param("status") MemberStatus status);


}
