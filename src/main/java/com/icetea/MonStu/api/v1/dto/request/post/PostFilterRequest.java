package com.icetea.MonStu.api.v1.dto.request.post;

import lombok.Builder;

import java.util.Date;

@Deprecated
@Builder
public record PostFilterRequest(

        Boolean isPublic,   // 공개여부
        String title,
        Long authorId, //작성자 id

        String dateOption,
        Date dateStart,
        Date dateEnd,

        Long viewCount,
        String viewCountOption  // "more", "less"
) { }
