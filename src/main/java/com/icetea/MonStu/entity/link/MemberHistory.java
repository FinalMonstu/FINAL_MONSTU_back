package com.icetea.MonStu.entity.link;

import com.icetea.MonStu.entity.History;
import com.icetea.MonStu.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"member","history"})
@Table(name="member_history")
public class MemberHistory {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "history_id")
    private History history;

}
