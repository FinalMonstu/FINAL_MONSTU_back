package com.icetea.MonStu.entity;

import com.icetea.MonStu.entity.log.PostLog;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"member","image","postLog"})
@DynamicUpdate
@Entity
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

    @ManyToMany
    @JoinTable(
            name = "post_history",
            joinColumns        = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "history_id")
    )
    private Set<History> histories = new HashSet<>();


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

}
