package com.icetea.MonStu.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.icetea.MonStu.enums.PostStatus;

import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record PostFilterRequest(

        Boolean isPublic,   // 공개여부
        String title,
        Long authorId, //작성자 id

        PostStatus status,

        String dateOption,
        Date dateStart,
        Date dateEnd,

        Long viewCount,
        String viewCountOption  // "more", "less"
) { }
