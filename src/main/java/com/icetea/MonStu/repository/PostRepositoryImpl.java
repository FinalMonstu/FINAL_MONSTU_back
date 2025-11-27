package com.icetea.MonStu.repository;

import com.icetea.MonStu.api.v2.dto.request.FilterPostRequest;
import com.icetea.MonStu.api.v2.dto.response.PostSummaryResponse;
import com.icetea.MonStu.entity.Post;
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

import static com.icetea.MonStu.entity.QMember.member;
import static com.icetea.MonStu.entity.QPost.post;
import static com.icetea.MonStu.entity.log.QPostLog.postLog;


@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom{

    private final JPAQueryFactory queryFactory;
    private final FilterPredicateManager predicateManager;

    @Override
    public Page<Post> findAllByFilter(FilterPostRequest filterDTO, Pageable pageable) {

        Predicate searchCondition = predicateManager.buildPostsFilterPredicate(filterDTO);

        List<Post> content = queryFactory
                .selectFrom(post)
                .leftJoin(post.postLog, postLog)
                .fetchJoin()
                .where(searchCondition)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(post.count())
                .from(post)
                .leftJoin(post.postLog, postLog)
                .where(searchCondition);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<PostSummaryResponse> findMyPostSummaries(Long memberId, Pageable pageable) {
        return searchPostSummaries(post.member.id.eq(memberId), pageable);
    }

    @Override
    public Page<PostSummaryResponse> findPublicPostSummaries(Pageable pageable) {
        return searchPostSummaries(post.isPublic.isTrue(), pageable);
    }

    /*----------------------------------------------------------------------------------------------------*/
    private JPAQuery<PostSummaryResponse> getPostSummaryBaseQuery() {
        return queryFactory
                .select(Projections.constructor(PostSummaryResponse.class,
                        post.id,
                        post.title,
                        post.createdAt,
                        post.modifiedAt,
                        post.isPublic,
                        post.member.id,
                        member.nickName
                ))
                .from(post)
                .leftJoin(post.member, member);
    }

    private Page<PostSummaryResponse> searchPostSummaries(Predicate condition, Pageable pageable) {
        List<PostSummaryResponse> content = getPostSummaryBaseQuery()
                .where(condition)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(post.count())
                .from(post)
                .where(condition);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }
}
