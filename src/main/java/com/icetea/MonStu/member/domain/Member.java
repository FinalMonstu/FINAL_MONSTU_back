package com.icetea.MonStu.member.domain;

import com.icetea.MonStu.post.domain.Post;
import com.icetea.MonStu.member.enums.CountryCode;
import com.icetea.MonStu.member.enums.MemberRole;
import com.icetea.MonStu.member.enums.MemberStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = {"memberLogs","posts"})
@Entity
@DynamicUpdate  // 수정된 필드만 update문에 반영
@Table(name= "member")
@EntityListeners(AuditingEntityListener.class)  // 자동 날짜 기입
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickName;

    @Column(nullable = false)
    private String phoneNumber;

    @CreatedDate
    @Column(updatable = false)
    private LocalDate createdAt;

    @LastModifiedDate
    private LocalDate updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberRole role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CountryCode countryCode;


    /*-----------------------------------연관관계-------------------------------------------*/

    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL,orphanRemoval = true, fetch = FetchType.LAZY)
    private List<MemberLog> memberLogs = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL,orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Post> posts = new ArrayList<>();


    /*-----------------------------------편의 메소드-------------------------------------------*/

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


    /*-----------------------------------Update 메소드-------------------------------------------*/
    public void changePassword(String encryptedPassword) {
        this.password = encryptedPassword;
    }

    public void changeStatus(MemberStatus status) {
        this.status = status;
    }

    public void updateProfile(String email,
                              String phoneNumber,
                              String nickName,
                              CountryCode countryCode,
                              MemberRole role,
                              MemberStatus status) {

        if (StringUtils.hasText(email)) this.email = email;
        if (StringUtils.hasText(phoneNumber)) this.phoneNumber = phoneNumber;
        if (StringUtils.hasText(nickName)) this.nickName = nickName;

        if (countryCode != null) this.countryCode = countryCode;
        if (role != null) this.role = role;
        if (status != null) this.status = status;
    }


}
