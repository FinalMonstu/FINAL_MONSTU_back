package com.icetea.MonStu.dto.request;

import com.icetea.MonStu.entity.link.PostTag;
import com.icetea.MonStu.enums.PostStatus;

import java.util.List;

public record PostRequest(
        Long id,
        String title,
        String content,
        PostStatus status,
        Boolean isPublic,
        String authorEmail,
        String nickName,
        List<PostTag> postTags
) { }
