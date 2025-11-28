package com.icetea.MonStu.post.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"post"})
@Entity
@Table(name="post_log")
public class PostLog {

    @Id  //post_id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId  // Post의 ID를 그대로 사용, 부모의 PK를 자신의 PK로 사용
    @JoinColumn(name = "post_id")
    private Post post;

    @Column
    private Long viewCount;

    @Column
    private LocalDate lastViewedAt;

    public void setPost(Post post) {
        this.post = post;
        if (post != null && post.getPostLog() != this) post.setPostLog(this);
    }

    public void removePost() {
        if (this.post != null) {
            this.post.setPostLog(null);
            this.post = null;
        }
    }
}
