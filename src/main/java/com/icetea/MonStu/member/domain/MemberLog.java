package com.icetea.MonStu.member.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = {"member"})
@DynamicUpdate
@Entity
@Table(name="member_log")
public class MemberLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Byte failedLoginCount;     // -128 ~ 127

    @Column
    private LocalDateTime failedLoginTime;

    @Column
    private Date lastLogin;


    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "member_id",nullable=true)
    private Member member;


    /*-----------------------------------편의 메소드-------------------------------------------*/

    public void setMember(Member member) {
        this.member = member;
        if(member != null && !member.getMemberLogs().contains(this)) member.getMemberLogs().add(this);
    }
}
