package com.icetea.MonStu.post.dto.v2.response;

import com.icetea.MonStu.post.domain.Post;
import com.icetea.MonStu.post.domain.PostLog;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record PostResponse(
        // Post Info
        Long id,

        String title,
        String content,

        LocalDate createdAt,
        LocalDate modifiedAt,

        Boolean isPublic,   // 공개여부

        //Member Info
        Long authorId,      //사용자 id
        String nickName,

        //Post Log Info
        Long logId,
        Long viewCount,

        LocalDate lastViewedAt
) {
    public static PostResponse toDto(Post e) {
        PostLog log = e.getPostLog();

        PostResponse.PostResponseBuilder response = builder()
                .id         (e.getId())
                .title      (e.getTitle())
                .content    (e.getContent())
                .createdAt  (e.getCreatedAt())
                .modifiedAt (e.getModifiedAt())
                .isPublic   (e.getIsPublic())

                .authorId (e.getMember().getId())
                .nickName (e.getMember().getNickName());

        if (log != null) {
            response.logId          (log.getId())
                    .viewCount      (log.getViewCount())
                    .lastViewedAt   (log.getLastViewedAt());
        }
        return response.build();
    }
}
