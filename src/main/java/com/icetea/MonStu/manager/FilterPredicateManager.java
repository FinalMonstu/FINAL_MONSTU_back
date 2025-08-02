package com.icetea.MonStu.manager;

import com.icetea.MonStu.api.v2.dto.request.FilterMemberRequest;
import com.icetea.MonStu.api.v2.dto.request.FilterPostRequest;
import com.icetea.MonStu.entity.QMember;
import com.icetea.MonStu.entity.QPost;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.querydsl.core.types.dsl.Expressions.allOf;

@Component
@RequiredArgsConstructor
public class FilterPredicateManager {

    /*---------------------------------------------------------Member----------------------------------------------------------------------------*/

    // MemberFilterRequest를 이용, 조건식 쿼리 작성-반환
    public static Predicate buildMembersFilterPredicate(FilterMemberRequest filterDTO) {
        QMember member = QMember.member;

        BooleanExpression predicate = allOf(
                StringUtils.hasText(filterDTO.email())
                        ? member.email.containsIgnoreCase(filterDTO.email())
                        : null,
                StringUtils.hasText(filterDTO.nickname())
                        ? member.nickName.containsIgnoreCase(filterDTO.nickname())
                        : null,
                filterDTO.countryCode() != null
                        ? member.countryCode.eq(filterDTO.countryCode())
                        : null,
                filterDTO.role() != null
                        ? member.role.eq(filterDTO.role())
                        : null,
                filterDTO.status() != null
                        ? member.status.eq(filterDTO.status())
                        : null
        );

        BooleanBuilder builder = new BooleanBuilder(predicate);
        if (StringUtils.hasText(filterDTO.dateOption()) && filterDTO.dateStart() != null && filterDTO.dateEnd() != null) {
            switch (filterDTO.dateOption()) {
                case "createdAt" -> builder
                        .and(member.createdAt.between(filterDTO.dateStart(), filterDTO.dateEnd()));
                case "updatedAt" -> builder
                        .and(member.updatedAt.between(filterDTO.dateStart(), filterDTO.dateEnd()));
            }
        }
        return builder;
    }


}
