package com.icetea.MonStu.repository.custom;

import com.icetea.MonStu.api.v2.dto.request.FilterPostRequest;
import com.icetea.MonStu.entity.Post;
import com.icetea.MonStu.entity.QPost;
import com.icetea.MonStu.repository.PostRepositoryCustom;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.querydsl.core.types.dsl.Expressions.allOf;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Long> findPostIdsByFilter(FilterPostRequest filterDTO, Pageable pageable) {
        QPost post = QPost.post;
        Predicate predicate = buildPostsFilterPredicate(filterDTO);

        return queryFactory
                .select (post.id)
                .from   (post)
                .where  (predicate)
                .offset (pageable.getOffset())
                .limit  (pageable.getPageSize())
                .fetch();
    }

    @Override
    public long countPostsByFilter(FilterPostRequest filterDTO) {
        QPost post = QPost.post;
        Predicate predicate = buildPostsFilterPredicate(filterDTO);

        Long count = queryFactory
                .select (post.count())
                .from   (post)
                .where  (predicate)
                .fetchOne();

        return count != null ? count : 0L;
    }

    @Override
    public List<Post> findPostsByIds(List<Long> ids) {
        QPost post = QPost.post;
        return queryFactory
                .selectFrom(post)
                .distinct() //중복제거
                .leftJoin(post.member) .fetchJoin()
                .leftJoin(post.postLog).fetchJoin()
//                .leftJoin(post.image).fetchJoin()
                .where(post.id.in(ids))
                .fetch();
    }

    // PostFilterRequest 이용, 조건식 쿼리 작성-반환
    protected Predicate buildPostsFilterPredicate(FilterPostRequest filterDTO) {
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
