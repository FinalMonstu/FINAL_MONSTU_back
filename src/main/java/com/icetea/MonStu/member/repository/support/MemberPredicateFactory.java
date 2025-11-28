package com.icetea.MonStu.member.repository.support;

import com.icetea.MonStu.member.dto.v2.request.FilterMemberRequest;
import com.icetea.MonStu.member.enums.CountryCode;
import com.icetea.MonStu.member.enums.MemberDateOption;
import com.icetea.MonStu.member.enums.MemberRole;
import com.icetea.MonStu.member.enums.MemberStatus;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDate;

import static com.icetea.MonStu.member.domain.QMember.member;
import static com.querydsl.core.types.dsl.Expressions.allOf;

@Component
public class MemberPredicateFactory {

    public Predicate buildMembersFilterPredicate(FilterMemberRequest filterDTO) {
        return allOf(
                containsMemberEmail(filterDTO.email()),
                containsMemberNickname(filterDTO.nickname()),
                eqMemberCountryCode(filterDTO.countryCode()),
                eqMemberRole(filterDTO.role()),
                eqMemberStatus(filterDTO.status()),
                betweenMemberDate(filterDTO.dateOption(), filterDTO.dateStart(), filterDTO.dateEnd())
        );
    }

    // --- Member 조건 메서드  ---

    private BooleanExpression containsMemberEmail(String email) {
        return StringUtils.hasText(email) ? member.email.containsIgnoreCase(email) : null;
    }

    private BooleanExpression containsMemberNickname(String nickname) {
        return StringUtils.hasText(nickname) ? member.nickName.containsIgnoreCase(nickname) : null;
    }

    private BooleanExpression eqMemberCountryCode(CountryCode countryCode) {
        return countryCode != null ? member.countryCode.eq(countryCode) : null;
    }

    private BooleanExpression eqMemberRole(MemberRole role) {
        return role != null ? member.role.eq(role) : null;
    }

    private BooleanExpression eqMemberStatus(MemberStatus status) {
        return status != null ? member.status.eq(status) : null;
    }

    private BooleanExpression betweenMemberDate(MemberDateOption option, LocalDate start, LocalDate end) {
        if (option == null || start == null || end == null) return null;

        return switch (option) {
            case CREATED_AT -> member.createdAt.between(start, end);
            case UPDATED_AT -> member.updatedAt.between(start, end);
        };
    }

}
