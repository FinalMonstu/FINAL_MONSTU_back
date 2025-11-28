package com.icetea.MonStu.post.dto.v2.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record UpdatePostRequest(

        @NotNull Long id,

        @NotBlank(message = "{PostRequest.title.NotBlank}")
        String title,

        @NotBlank(message = "{PostRequest.title.NotBlank}")
        String content,

        @NotNull Boolean isPublic
) {

}
