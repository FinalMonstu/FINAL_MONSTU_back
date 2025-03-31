package com.icetea.MonStu.dto;

import com.icetea.MonStu.enums.PostStatus;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PostDTO{
    private Long id;
    private String title;
    private String content;
    private Date createdAt;
    private Date modifiedAt;
    private PostStatus status;
    private Boolean isPublic;   // 공개여부

    //Member Info
    private Long authorId; //사용자 이메일
    private String nickName;

    //Tag Info
    private List<TagDTO> tags;
}
