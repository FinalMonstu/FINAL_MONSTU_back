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

        //Member Info
        String authorEmail,
        String nickName,

        //Tag Info
        List<PostTag> postTags
) { }
