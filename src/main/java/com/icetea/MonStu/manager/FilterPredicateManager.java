package com.icetea.MonStu.manager;

import com.icetea.MonStu.api.v1.dto.request.member.MemberFilterRequest;
import com.icetea.MonStu.api.v1.dto.request.post.PostFilterRequest;
import com.icetea.MonStu.entity.QMember;
import com.icetea.MonStu.entity.QPost;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import static com.querydsl.core.types.dsl.Expressions.allOf;

@Component
@RequiredArgsConstructor
public class FilterPredicateManager {

    // MemberFilterRequest를 이용, 조건식 쿼리 작성-반환
    public static Predicate buildMembersFilterPredicate(MemberFilterRequest filterDTO) {
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


    // PostFilterRequest 이용, 조건식 쿼리 작성-반환
    public static Predicate buildPostsFilterPredicate(PostFilterRequest filterDTO) {
        QPost post = QPost.post;

        BooleanExpression predicate = allOf(
                filterDTO.isPublic() != null
                        ? post.isPublic.eq(filterDTO.isPublic())
                        : null,
                StringUtils.hasText(filterDTO.title())
                        ? post.title.containsIgnoreCase(filterDTO.title())
                        : null,
                filterDTO.authorId() != null
                        ? post.member.id.eq(filterDTO.authorId())
                        : null
        );

        BooleanBuilder builder = new BooleanBuilder(predicate);

        if (StringUtils.hasText(filterDTO.dateOption()) && filterDTO.dateStart() != null && filterDTO.dateEnd() != null) {
            switch (filterDTO.dateOption()) {
                case "createdAt" -> builder
                        .and(post.createdAt.between(filterDTO.dateStart(), filterDTO.dateEnd()));
                case "modifiedAt" -> builder
                        .and(post.modifiedAt.between(filterDTO.dateStart(), filterDTO.dateEnd()));
                case "lastViewedAt" -> builder
                        .and(post.postLog.lastViewedAt.between(filterDTO.dateStart(), filterDTO.dateEnd()));
            }
        }

        if (filterDTO.viewCount() != null && filterDTO.viewCountOption() != null) {
            switch (filterDTO.viewCountOption()) {
                case "more" -> builder
                        .and(post.postLog.viewCount.goe(filterDTO.viewCount()));
                case "less" -> builder
                        .and(post.postLog.viewCount.loe(filterDTO.viewCount()));
            }
        }
        return builder;
    }
}
