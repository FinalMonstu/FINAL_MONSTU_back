package com.icetea.MonStu.post.repository.support;

import com.icetea.MonStu.post.dto.v2.request.FilterPostRequest;
import com.icetea.MonStu.post.enums.PostDateOption;
import com.icetea.MonStu.post.enums.ViewCountOption;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDate;

import static com.icetea.MonStu.post.domain.QPost.post;
import static com.icetea.MonStu.post.domain.QPostLog.postLog;
import static com.querydsl.core.types.dsl.Expressions.allOf;

@Component
public class PostPredicateFactory {

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
