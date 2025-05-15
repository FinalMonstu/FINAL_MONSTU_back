package com.icetea.MonStu.dto.request;

import com.icetea.MonStu.entity.link.PostTag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record PostRequest(
        Long id,

        @NotBlank(message = "{PostRequest.title.NotBlank}")
        String title,

        @NotBlank(message = "{PostRequest.content.NotBlank}")
        String content,

        Boolean isPublic,

        //Member Info
        @NotNull(message = "{PostRequest.authorEmail.NotNull}")
        String authorEmail,
        String nickName,

        //Tag Info
        List<PostTag> postTags
) { }
