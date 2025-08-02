package com.icetea.MonStu.entity;

import com.icetea.MonStu.entity.link.MemberPostHistory;
import com.icetea.MonStu.entity.log.MemberLog;
import com.icetea.MonStu.enums.CountryCode;
import com.icetea.MonStu.enums.MemberRole;
import com.icetea.MonStu.enums.MemberStatus;
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
@ToString(exclude = {"memberLogs","posts","memberPostHistories"})
@Entity
@DynamicUpdate  // 수정된 필드만 update문에 반영
@Table(name= "member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    @Column(unique = true)
    private String email;

    @Column @NotBlank
    private String password;

    @Column @NotBlank
    private String nickName;

    @Column @NotBlank
    private String phoneNumber;

    @Column
    private Date createdAt;

    @Column
    private Date updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberRole role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CountryCode countryCode;

    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL,orphanRemoval = true, fetch = FetchType.LAZY)
    private List<MemberLog> memberLogs = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL,orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Post> posts = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL,orphanRemoval = true, fetch = FetchType.LAZY)
    private List<MemberPostHistory> memberPostHistories = new ArrayList<>();


    public void addMemberLog(MemberLog memberLog){
        this.memberLogs.add(memberLog);
        if(memberLog != null && memberLog.getMember()!=this) memberLog.setMember(this);
    }

    public void removeMemberLog(MemberLog memberLog){
        memberLogs.remove(memberLog);
        if(memberLog != null && memberLog.getMember()==this) memberLog.setMember(null);
    }

    public void addPosts(Post post){
        this.posts.add(post);
        if(post != null && post.getMember()!=this) post.setMember(this);
    }

    public void removePost(Post post){
        posts.remove(post);
        if(post != null && post.getMember()==this) post.setMember(null);
    }
}
