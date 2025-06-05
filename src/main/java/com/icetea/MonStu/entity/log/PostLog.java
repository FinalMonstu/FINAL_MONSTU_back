package com.icetea.MonStu.entity.log;

import com.icetea.MonStu.entity.Post;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

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
    @MapsId  // Post의 ID를 그대로 사용
    @JoinColumn(name = "post_id")
    private Post post;

    @Column
    private Long viewCount;

    @Column
    private Date lastViewedAt;

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
