package com.icetea.MonStu.api.v2.mapper;

import com.icetea.MonStu.api.v2.dto.request.CreatePostRequest;
import com.icetea.MonStu.api.v2.dto.request.PostRequest;
import com.icetea.MonStu.api.v2.dto.request.UpdatePostRequest;
import com.icetea.MonStu.entity.Member;
import com.icetea.MonStu.entity.Post;

import java.util.Date;

public final class PostMapper {

    private PostMapper() {}


    public static Post toEntity(CreatePostRequest request, Member member){
        return Post.builder()
                .id(request.id())
                .title(request.title())
                .content(request.content())
                .isPublic(request.isPublic())
                .createdAt(new Date())
                .member(member)
                .build();
    }

    public static void updateFromDto(Post post, UpdatePostRequest req) {
        post.setTitle(req.title());
        post.setContent(req.content());
        post.setIsPublic(req.isPublic());
        post.setModifiedAt(new Date());
    }

}
