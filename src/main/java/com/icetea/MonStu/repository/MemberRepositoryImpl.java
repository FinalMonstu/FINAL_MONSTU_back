package com.icetea.MonStu.repository;

import com.icetea.MonStu.api.v2.dto.request.FilterMemberRequest;
import com.icetea.MonStu.api.v2.dto.response.MemberSummaryResponse;
import com.icetea.MonStu.manager.FilterPredicateManager;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;
import java.util.Optional;

import static com.icetea.MonStu.entity.QMember.member;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final FilterPredicateManager predicateManager;

    @Override
    public Page<MemberSummaryResponse> findAllByFilter(FilterMemberRequest filterDTO, Pageable pageable) {

        Predicate searchCondition = predicateManager.buildMembersFilterPredicate(filterDTO);

        List<MemberSummaryResponse> content = getMemberBaseQuery()
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

    @Override
    public Optional<MemberSummaryResponse> findMemberDtoById(Long memberId) {
        MemberSummaryResponse content = getMemberBaseQuery()
                .where(member.id.eq(memberId))
                .fetchOne();
        return Optional.ofNullable(content);
    }

    /*---------------------------------------------BaseQuery-------------------------------------------*/
    private JPAQuery<MemberSummaryResponse> getMemberBaseQuery() {
        return queryFactory
                .select(Projections.constructor(MemberSummaryResponse.class,
                        member.id,
                        member.email,
                        member.nickName,
                        member.phoneNumber,
                        member.createdAt,
                        member.updatedAt,
                        member.status,
                        member.role,
                        member.countryCode
                ))
                .from(member);
    }
}
