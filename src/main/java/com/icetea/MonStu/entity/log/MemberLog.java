package com.icetea.MonStu.entity.log;

import com.icetea.MonStu.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Date;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"member"})
@Entity
@Table(name="member_log")
public class MemberLog {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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

    public void setMember(Member member) {
        this.member = member;
        if(!member.getMemberLogs().contains(this)) member.getMemberLogs().add(this);
    }
}
