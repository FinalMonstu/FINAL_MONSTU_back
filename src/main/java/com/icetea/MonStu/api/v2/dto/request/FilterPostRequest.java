package com.icetea.MonStu.api.v2.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

@Builder
public record FilterPostRequest(

        Boolean isPublic,   // 공개여부
        String  title,
        Long    authorId,   //작성자 id

        String dateOption,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate   dateStart,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate dateEnd,

        Long   viewCount,
        String viewCountOption  // "more", "less"
) { }
