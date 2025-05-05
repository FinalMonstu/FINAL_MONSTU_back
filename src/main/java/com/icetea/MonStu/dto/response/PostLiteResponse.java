package com.icetea.MonStu.dto.response;

import com.icetea.MonStu.enums.PostStatus;

import java.util.Date;

public record PostLiteResponse(
         Long id,
         String title,
         Date createdAt,
         Date modifiedAt,
         PostStatus status,
         Boolean isPublic,   // 공개여부

        //Member Info
         Long authorId, //사용자 id
         String nickName
){ }
