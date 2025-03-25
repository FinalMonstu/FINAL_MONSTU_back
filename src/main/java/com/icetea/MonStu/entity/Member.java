package com.icetea.MonStu.entity;

import com.icetea.MonStu.entity.link.MemberHistory;
import com.icetea.MonStu.entity.log.MemberLog;
import com.icetea.MonStu.entity.log.PostLog;
import com.icetea.MonStu.enums.MemberRole;
import com.icetea.MonStu.enums.MemberStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"memberLogs","posts","postLog","memberHistories"})
@Table(name= "member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String pw;

    @Column(nullable = false, unique = true)
    private String nickName;

    @Column
    private Date createdAt;

    @Column
    private Date updatedAt;

    @Column(nullable = false)
    private MemberStatus status;

    @Column(nullable = false)
    private MemberRole role;


    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<MemberLog> memberLogs = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Post> posts = new ArrayList<>();

    @OneToOne(mappedBy = "post", fetch = FetchType.EAGER)
    private PostLog postLog;

    // 다대다 연관관계
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MemberHistory> memberHistories = new ArrayList<>();




    public void addMemberLog(MemberLog memberLog){
        this.memberLogs.add(memberLog);
        if(memberLog.getMember()!=this) memberLog.setMember(this);
    }

    public void removeMemberLog(MemberLog memberLog){
        memberLogs.remove(memberLog);
        if(memberLog.getMember()==this) memberLog.setMember(null);
    }

    public void addPosts(Post post){
        this.posts.add(post);
        if(post.getMember()!=this) post.setMember(this);
    }

    public void removePost(Post post){
        posts.remove(post);
        if(post.getMember()==this) post.setMember(null);
    }
}
