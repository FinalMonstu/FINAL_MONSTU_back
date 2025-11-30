package com.icetea.MonStu.member.scheduler;

import com.icetea.MonStu.member.application.MemberService;
import com.icetea.MonStu.member.application.MemberLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class MemberScheduler {

    private final MemberService memberSvc;
    private final MemberLogService memberLogSvc;

    // 매일 00:00 (자정)에 실행   cron = "초 분 시 일 월 요일"
    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    public void runDailyTask() {
        long startTime = System.currentTimeMillis();
        log.info("[Scheduler] 탈퇴 회원 일괄 삭제 작업을 시작합니다.");

        try {
            /* 멤버 삭제 */
            // Member, MemberLog 테이블 연결 제거
            memberLogSvc.deleteMembersAndUnlinkLogs();
            // Member 'Status'속성 'DELETE'인 모든 사용자 삭제
            memberSvc.purgeDeletedMembers();

            long endTime = System.currentTimeMillis();
            log.info("[Scheduler] 작업 완료. 소요시간: {}ms", (endTime - startTime));

        } catch (Exception e) {
            log.error("[Scheduler] 탈퇴 회원 삭제 중 에러 발생", e);
        }
    }

}
