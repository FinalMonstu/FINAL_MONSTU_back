package com.icetea.MonStu.aop;

import com.icetea.MonStu.service.MemberService;
import com.icetea.MonStu.service.log.MemberLogService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Aspect
@RequiredArgsConstructor
@Component
public class scheduledMember {

    private final MemberService memberSvc;
    private final MemberLogService memberLogSvc;

    // 매일 00:00 (자정)에 실행   cron = "초 분 시 일 월 요일"
    @Scheduled(cron = "0 00 00 * * *", zone = "Asia/Seoul")
    public void runDailyTask() {

        // Member, MemberLog 테이블 연결 제거
        memberLogSvc.deleteMembersAndUnlinkLogs();
        // Member 'Status'속성 'DELETE'인 모든 사용자 삭제
        memberSvc.deleteMembers();

    }
}
