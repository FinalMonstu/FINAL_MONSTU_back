package com.icetea.MonStu.dto.request.post;

import lombok.Builder;

import java.util.Date;

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
