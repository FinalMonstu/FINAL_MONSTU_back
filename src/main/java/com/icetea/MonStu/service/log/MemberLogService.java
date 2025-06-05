package com.icetea.MonStu.service.log;

import com.icetea.MonStu.enums.MemberStatus;
import com.icetea.MonStu.repository.MemberRepository;
import com.icetea.MonStu.repository.log.MemberLogRepository;
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
