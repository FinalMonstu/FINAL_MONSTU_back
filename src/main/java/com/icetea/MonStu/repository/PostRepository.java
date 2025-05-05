package com.icetea.MonStu.repository;

import com.icetea.MonStu.entity.Member;
import com.icetea.MonStu.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface PostRepository extends JpaRepository<Post,Long>, QuerydslPredicateExecutor<Post> {
}
