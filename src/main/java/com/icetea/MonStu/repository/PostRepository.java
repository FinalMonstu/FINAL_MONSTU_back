package com.icetea.MonStu.repository;

import com.icetea.MonStu.entity.Post;
import com.icetea.MonStu.enums.MemberStatus;
import com.icetea.MonStu.enums.PostStatus;
import com.querydsl.core.types.Projections;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post,Long>, QuerydslPredicateExecutor<Post> {
    @EntityGraph(attributePaths = {"member", "postLog"})
    Optional<Post> findWithMemberAndLogById(Long id);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE Post p SET p.status = :status WHERE p.id IN :ids")
    void updateStatusById( @Param("ids") List<Long> ids,
                           @Param("status") PostStatus postStatus);

}
