package com.icetea.MonStu.member.repository;

import com.icetea.MonStu.member.domain.Member;
import com.icetea.MonStu.member.enums.MemberStatus;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

import static com.icetea.MonStu.member.domain.QMember.member;
import static com.icetea.MonStu.member.domain.QMemberLog.memberLog;

@RequiredArgsConstructor
public class MemberLogRepositoryImpl implements MemberLogRepositoryCustom{

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    @Override
    public long nullifyLogsByMemberStatus(MemberStatus status) {
        long count = queryFactory
                .update(memberLog)
                .set(memberLog.member, (Member) null) // member를 null로 설정
                .where(memberLog.member.id.in(
                        JPAExpressions
                                .select(member.id)
                                .from(member)
                                .where(member.status.eq(status))
                ))
                .execute();

        em.flush();
        em.clear();

        return count;
    }

}
