package com.icetea.MonStu.entity;

import com.icetea.MonStu.entity.link.MemberPostHistory;
import com.icetea.MonStu.entity.log.PostLog;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"member","image"})
@Entity
@DynamicUpdate
@Table(name="post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column @NotBlank
    private String title;

    @NotBlank
    @Column(columnDefinition = "TEXT")
    private String content;

    @Column
    private Date createdAt;

    @Column
    private Date modifiedAt;

    @Column(nullable = false)
    private Boolean isPublic;   // 공개여부


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id")
    private Member member;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true )
    @JoinColumn(name = "thumbnail_id")
    private Image image;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true )
    @JoinColumn(name = "post_log_id")
    private PostLog postLog;


    // 다대다 연관관계
    @Builder.Default
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL,orphanRemoval = true, fetch = FetchType.LAZY)
    private List<MemberPostHistory> memberPostHistories = new ArrayList<>();

    public void setMember(Member member) {
        this.member = member;
        if(!member.getPosts().contains(this)) member.getPosts().add(this);
    }

    public void setPostLog(PostLog postLog) {
        this.postLog = postLog;
        if (postLog != null && postLog.getPost() != this)  postLog.setPost(this);
    }

    public void removePostLog() {
        if (this.postLog != null) {
            this.postLog.setPost(null);
            this.postLog = null;
        }
    }
}
