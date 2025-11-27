package com.icetea.MonStu.manager;

import com.icetea.MonStu.api.v2.dto.request.FilterMemberRequest;
import com.icetea.MonStu.api.v2.dto.request.FilterPostRequest;
import com.icetea.MonStu.enums.*;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDate;

import static com.icetea.MonStu.entity.QMember.member;
import static com.icetea.MonStu.entity.QPost.post;
import static com.icetea.MonStu.entity.log.QPostLog.postLog;
import static com.querydsl.core.types.dsl.Expressions.allOf;

@Component
public class FilterPredicateManager {

    /*---------------------------------------------------------Member----------------------------------------------------------------------------*/

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

    /*---------------------------------------------------------Post----------------------------------------------------------------------------*/

    public Predicate buildPostsFilterPredicate(FilterPostRequest filterDTO) {
        return allOf(
                eqPostIsPublic(filterDTO.isPublic()),
                containsPostTitle(filterDTO.title()),
                eqPostAuthorId(filterDTO.authorId()),
                betweenPostDate(filterDTO.dateOption(), filterDTO.dateStart(), filterDTO.dateEnd()),
                comparePostViewCount(filterDTO.viewCount(), filterDTO.viewCountOption())
        );
    }

    // --- Post 조건 메서드  ---

    private BooleanExpression eqPostIsPublic(Boolean isPublic) {
        return isPublic != null ? post.isPublic.eq(isPublic) : null;
    }

    private BooleanExpression containsPostTitle(String title) {
        return StringUtils.hasText(title) ? post.title.containsIgnoreCase(title) : null;
    }

    private BooleanExpression eqPostAuthorId(Long authorId) {
        return authorId != null ? post.member.id.eq(authorId) : null;
    }

    private BooleanExpression betweenPostDate(PostDateOption option, LocalDate start, LocalDate end) {
        if (option == null || start == null || end == null)  return null;

        return switch (option) {
            case CREATED_AT -> post.createdAt.between(start, end);
            case MODIFIED_AT -> post.modifiedAt.between(start, end);
            case LAST_VIEWED_AT -> postLog.lastViewedAt.between(start, end);
        };
    }

    private BooleanExpression comparePostViewCount(Long count, ViewCountOption option) {
        if (count == null || option == null)  return null;

        return switch (option) {
            case MORE -> postLog.viewCount.goe(count);
            case LESS -> postLog.viewCount.loe(count);
        };
    }
}