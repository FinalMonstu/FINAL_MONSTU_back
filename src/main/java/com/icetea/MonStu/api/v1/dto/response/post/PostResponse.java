package com.icetea.MonStu.api.v1.dto.response.post;

import com.icetea.MonStu.entity.Post;
import com.icetea.MonStu.entity.log.PostLog;
import lombok.Builder;

import java.util.Date;

@Builder
public record PostResponse(
        // Post Info
        Long id,

        String title,
        String content,

        Date createdAt,
        Date modifiedAt,

        Boolean isPublic,   // 공개여부

        //Member Info
        Long authorId, //사용자 id
        String nickName,

        //Post Log Info
        Long logId,
        Long viewCount,

        Date lastViewedAt
) {
    public static PostResponse toDto(Post e) {
        PostLog log = e.getPostLog();

        PostResponse.PostResponseBuilder response = builder()
                .id(e.getId())
                .title(e.getTitle())
                .content(e.getContent())
                .createdAt(e.getCreatedAt())
                .modifiedAt(e.getModifiedAt())
                .isPublic(e.getIsPublic())

                .authorId(e.getMember().getId())
                .nickName(e.getMember().getNickName());

        if (log != null) {
            response.logId(log.getId())
                    .viewCount(log.getViewCount())
                    .lastViewedAt(log.getLastViewedAt());
        }
        return response.build();
    }
}
