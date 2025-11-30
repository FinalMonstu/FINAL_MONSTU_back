package com.icetea.MonStu.post.repository;

import com.icetea.MonStu.post.domain.Post;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post,Long>, QuerydslPredicateExecutor<Post>, PostRepositoryCustom  {

//    @EntityGraph(attributePaths = {"member"})
//    Page<Post> readByIsPublicTrue(Pageable pageable);

    @EntityGraph(attributePaths = {"member", "postLog"})
    Optional<Post> readPostWithLogById(Long id);


}
