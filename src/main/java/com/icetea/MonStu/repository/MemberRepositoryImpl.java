package com.icetea.MonStu.repository;

import com.icetea.MonStu.api.v2.dto.request.FilterMemberRequest;
import com.icetea.MonStu.entity.Member;
import com.icetea.MonStu.manager.FilterPredicateManager;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.icetea.MonStu.entity.QMember.member;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final FilterPredicateManager predicateManager;

    @Override
    public Page<Member> findAllByFilter(FilterMemberRequest filterDTO, Pageable pageable) {

        Predicate searchCondition = predicateManager.buildMembersFilterPredicate(filterDTO);

        List<Member> content = queryFactory
                .selectFrom(member)
                .where(searchCondition)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(member.count())
                .from(member)
                .where(searchCondition);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

}
