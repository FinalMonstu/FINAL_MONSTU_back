package com.icetea.MonStu.member.application;

import com.icetea.MonStu.member.enums.MemberStatus;
import com.icetea.MonStu.member.repository.MemberLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberLogService {
    private final MemberLogRepository memberLogRps;

    // Member의 Status가 'DELETE'인 요소의 member_id를 null로 변경
    @Transactional
    public void deleteMembersAndUnlinkLogs() {
        memberLogRps.nullifyLogsByMemberStatus(MemberStatus.DELETED);
    }
}
