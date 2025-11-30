package com.icetea.MonStu.post.application;

import com.icetea.MonStu.post.dto.v2.request.CreatePostRequest;
import com.icetea.MonStu.member.domain.Member;
import com.icetea.MonStu.post.domain.Post;

import java.time.LocalDate;

public final class PostMapper {

    private PostMapper() {}


    public static Post toEntity(CreatePostRequest request, Member member){
        return Post.builder()
                .id(request.id())
                .title(request.title())
                .content(request.content())
                .isPublic(request.isPublic())
                .createdAt(LocalDate.now())
//                .authorId( (request.authorId()!=null) ? request.authorId() : member.getId() )
                .member(member)
                .build();
    }

}
