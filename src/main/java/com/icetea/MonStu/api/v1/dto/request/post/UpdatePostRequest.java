package com.icetea.MonStu.api.v1.dto.request.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Deprecated
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
