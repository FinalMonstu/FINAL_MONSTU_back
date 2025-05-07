package com.icetea.MonStu.dto.response;

import com.icetea.MonStu.entity.Post;
import com.icetea.MonStu.enums.PostStatus;
import lombok.Builder;

import java.util.Date;

@Builder
public record PostLiteResponse(
         // Post Info
         Long id,
         String title,
         Date createdAt,
         Date modifiedAt,
         PostStatus status,
         Boolean isPublic,   // 공개여부

         //Member Info
         Long authorId, //사용자 id
         String nickName
){
    public static PostLiteResponse mapper(Post p) {
        return builder()
                // Post Info
                .id(p.getId())
                .title(p.getTitle())
                .createdAt(p.getCreatedAt())
                .modifiedAt(p.getModifiedAt())
                .status(p.getStatus())
                .isPublic(p.getIsPublic())

                // Member Info
                .authorId(p.getMember().getId())
                .nickName(p.getMember().getNickName())

                .build();
    }
}
