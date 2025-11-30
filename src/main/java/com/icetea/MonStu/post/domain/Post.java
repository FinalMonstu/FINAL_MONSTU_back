package com.icetea.MonStu.post.domain;

import com.icetea.MonStu.history.domain.History;
import com.icetea.MonStu.image.domain.Image;
import com.icetea.MonStu.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = {"member","image","postLog"})
@DynamicUpdate
@Entity
@Table(name="post")
@EntityListeners(AuditingEntityListener.class)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT",nullable = false)
    private String content;

    @CreatedDate
    @Column(updatable = false)
    private LocalDate createdAt;

    @LastModifiedDate
    private LocalDate modifiedAt;

    @Builder.Default
    @Column(nullable = false)
    private Boolean isPublic = false;   // 공개여부


    /*-----------------------------------연관관계-------------------------------------------*/

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id")
    private Member member;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true )
    @JoinColumn(name = "thumbnail_id")
    private Image image;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true )
    @JoinColumn(name = "post_log_id")
    private PostLog postLog;

    @ManyToMany
    @JoinTable(
            name = "post_history",
            joinColumns        = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "history_id")
    )
    private Set<History> histories = new HashSet<>();


    /*-----------------------------------편의 메소드-------------------------------------------*/

    public void setMember(Member member) {
        this.member = member;
        if(member != null && !member.getPosts().contains(this)) member.getPosts().add(this);
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

    public void addHistory(History history) {
        if (history == null) return;
        if (!this.histories.contains(history)) {
            this.histories.add(history);
            if (!history.getPosts().contains(this)) {
                history.getPosts().add(this);
            }
        }
    }

    public void removeHistory(History history) {
        if (history == null) return;
        if (this.histories.contains(history)) {
            this.histories.remove(history);
            if (history.getPosts().contains(this)) {
                history.getPosts().remove(this);
            }
        }
    }


    /*-----------------------------------Update 메소드-------------------------------------------*/

    public void update(String title, String content, Boolean isPublic) {
        if (StringUtils.hasText(title))   this.title = title;
        if (StringUtils.hasText(content)) this.content = content;
        if (isPublic != null)             this.isPublic = isPublic;
    }


    /*-----------------------------------검증 메소드-------------------------------------------*/

    public boolean isOwner(Long userId) {
        if (userId == null) return false;
        return Objects.equals(this.member.getId(), userId);
    }
}
