package com.icetea.MonStu.dto.request;

import com.icetea.MonStu.enums.PostStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record UpdatePostRequest(
        Long id,

        @NotBlank(message = "{PostRequest.title.NotBlank}")
        String title,

        @NotBlank(message = "{PostRequest.title.NotBlank}")
        String content,

        PostStatus status,

        @NotNull
        Boolean isPublic
) {

}
