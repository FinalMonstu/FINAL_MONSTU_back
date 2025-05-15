package com.icetea.MonStu.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.icetea.MonStu.entity.Post;
import com.icetea.MonStu.entity.log.PostLog;
import com.icetea.MonStu.enums.PostStatus;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Date;

@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record PostResponse(
        // Post Info
        Long postId,

        String title,
        String content,

        Date createdAt,
        Date modifiedAt,

        PostStatus status,
        Boolean isPublic,   // 공개여부

        //Member Info
        Long authorId, //사용자 id
        String nickName,

        //Post Log Info
        Long logId,
        Long viewCount,

        Date lastViewedAt
) {
    public static PostResponse mapper(Post e) {
        PostLog log = e.getPostLog();
        Long logId       = (log != null ? log.getId()       : null);
        Long viewCount   = (log != null ? log.getViewCount(): 0L);
        Date lastViewedAt= (log != null ? log.getLastViewedAt(): null);

        return builder()
                .postId(e.getId())
                .title(e.getTitle())
                .content(e.getContent())
                .createdAt(e.getCreatedAt())
                .modifiedAt(e.getModifiedAt())
                .status(e.getStatus())
                .isPublic(e.getIsPublic())

                .authorId(e.getMember().getId())
                .nickName(e.getMember().getNickName())

                .logId(logId)
                .viewCount(viewCount)
                .lastViewedAt(lastViewedAt)

                .build();
    }
}
