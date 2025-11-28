package com.icetea.MonStu.post.dto.v2.response;

import com.icetea.MonStu.post.domain.Post;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record PostSummaryResponse(
         // Post Info
         Long id,
         String title,

         LocalDate createdAt,
         LocalDate modifiedAt,

         Boolean isPublic,   // 공개여부

         //Member Info
         Long authorId, //사용자 id
         String nickName
){
    public static PostSummaryResponse toDto(Post p) {
        return builder()

                // Post Info
                .id         (p.getId())
                .title      (p.getTitle())
                .createdAt  (p.getCreatedAt())
                .modifiedAt (p.getModifiedAt())
                .isPublic   (p.getIsPublic())

                // Member Info
                .authorId   (p.getMember().getId())
                .nickName   (p.getMember().getNickName())

                .build();
    }
}
