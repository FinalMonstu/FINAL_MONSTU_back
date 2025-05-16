package com.icetea.MonStu.dto.response.post;

import com.icetea.MonStu.entity.Post;
import lombok.Builder;

import java.util.Date;

@Builder
public record PostLiteResponse(
         // Post Info
         Long id,
         String title,

         Date createdAt,
         Date modifiedAt,

         Boolean isPublic,   // 공개여부

         //Member Info
         Long authorId, //사용자 id
         String nickName
){
    public static PostLiteResponse toDto(Post p) {
        return builder()
                // Post Info
                .id(p.getId())
                .title(p.getTitle())
                .createdAt(p.getCreatedAt())
                .modifiedAt(p.getModifiedAt())
                .isPublic(p.getIsPublic())

                // Member Info
                .authorId(p.getMember().getId())
                .nickName(p.getMember().getNickName())

                .build();
    }
}
