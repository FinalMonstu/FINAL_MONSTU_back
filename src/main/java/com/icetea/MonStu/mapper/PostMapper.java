package com.icetea.MonStu.mapper;

import com.icetea.MonStu.dto.request.post.PostRequest;
import com.icetea.MonStu.dto.request.post.UpdatePostRequest;
import com.icetea.MonStu.entity.Member;
import com.icetea.MonStu.entity.Post;

import java.util.Date;

public final class PostMapper {
    private PostMapper() {}


    public static Post toEntity(PostRequest request, Member member){
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
