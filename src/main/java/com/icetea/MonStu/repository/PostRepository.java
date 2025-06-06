package com.icetea.MonStu.repository;

import com.icetea.MonStu.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post,Long>, QuerydslPredicateExecutor<Post> {

    @EntityGraph(attributePaths = {"member"})
    Page<Post> findByMember_Id(Long memberId, Pageable pageable);

    @EntityGraph(attributePaths = {"member", "postLog"})
    Optional<Post> findWithMemberAndLogById(Long id);

    @EntityGraph(attributePaths = {"member"})
    Page<Post> findByIsPublicTrue(Pageable pageable);
}
