package com.icetea.MonStu.entity;

import com.icetea.MonStu.entity.link.MemberPostHistory;
import com.icetea.MonStu.entity.link.PostTag;
import com.icetea.MonStu.enums.PostStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"member","image","postTags"})
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
    @Enumerated(EnumType.STRING)
    private PostStatus status;

    @Column(nullable = false)
    private Boolean isPublic;   // 공개여부


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id")
    private Member member;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true )
    @JoinColumn(name = "thumbnail_id")
    private Image image;

    // 다대다 연관관계
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL,orphanRemoval = true, fetch = FetchType.LAZY)
    private List<PostTag> postTags = new ArrayList<>();

    // 다대다 연관관계
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL,orphanRemoval = true, fetch = FetchType.LAZY)
    private List<MemberPostHistory> memberPostHistories = new ArrayList<>();

    public void setMember(Member member) {
        this.member = member;
        if(!member.getPosts().contains(this)) member.getPosts().add(this);
    }


}
