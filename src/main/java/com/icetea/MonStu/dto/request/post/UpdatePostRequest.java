package com.icetea.MonStu.dto.request.post;

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
