package com.icetea.MonStu.api.v2.dto.request;

import lombok.Builder;

import java.util.Date;

@Builder
public record FilterPostRequest(

        Boolean isPublic,   // 공개여부
        String title,
        Long authorId, //작성자 id

        String dateOption,
        Date dateStart,
        Date dateEnd,

        Long viewCount,
        String viewCountOption  // "more", "less"
) { }
