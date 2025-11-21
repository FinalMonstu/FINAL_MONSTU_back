package com.icetea.MonStu.repository;

import com.icetea.MonStu.api.v2.dto.request.FilterPostRequest;
import com.icetea.MonStu.api.v2.dto.response.PostResponse;
import com.icetea.MonStu.entity.Post;
import com.icetea.MonStu.manager.FilterPredicateManager;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.icetea.MonStu.entity.QPost;
import com.icetea.MonStu.entity.log.QPostLog;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

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
}
