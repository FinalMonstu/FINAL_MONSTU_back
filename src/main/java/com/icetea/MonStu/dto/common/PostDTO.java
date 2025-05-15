package com.icetea.MonStu.dto.common;

import com.icetea.MonStu.entity.Member;
import com.icetea.MonStu.entity.Post;
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
    private String authorEmail; //사용자 이메일
    private String nickName;

    //Tag Info
    private List<TagDTO> tags;


    public static PostDTO mapper(Post p) {
        return builder()
                .id(p.getId())
                .title(p.getTitle())
                .content(p.getContent())
                .createdAt(p.getCreatedAt())
                .modifiedAt(p.getModifiedAt())
                .status(p.getStatus())
                .isPublic(p.getIsPublic())

                .authorEmail(p.getMember().getEmail())
                .nickName(p.getMember().getNickName())

                .build();
    }
}
