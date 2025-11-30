package com.icetea.MonStu.post.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = {"post"})
@Entity
@Table(name="post_log")
public class PostLog {

    @Id  //post_id
    private Long id;

    @Builder.Default
    @Column(nullable = false)
    private Long viewCount = 0L;

    @Column
    private LocalDate lastViewedAt;


    /*-----------------------------------연관관계-------------------------------------------*/

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId  // Post의 ID를 그대로 사용, 부모의 PK를 자신의 PK로 사용
    @JoinColumn(name = "post_id")
    private Post post;


    /*-----------------------------------편의 메소드-------------------------------------------*/

    public void setPost(Post post) {
        this.post = post;
        if (post != null && post.getPostLog() != this) post.setPostLog(this);
    }

}
