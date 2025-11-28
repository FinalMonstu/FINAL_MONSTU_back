package com.icetea.MonStu.post.dto.v2.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PostRequest(
        Long id,

        @NotBlank(message = "{PostRequest.title.NotBlank}")
        String title,

        @NotBlank(message = "{PostRequest.content.NotBlank}")
        String content,

        Boolean isPublic,

        //Member Info
        @NotNull(message = "{PostRequest.authorEmail.NotNull}")
        Long authorId,
        String nickName

) { }
