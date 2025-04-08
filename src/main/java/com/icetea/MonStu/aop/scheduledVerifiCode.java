package com.icetea.MonStu.aop;

import com.icetea.MonStu.repository.VerifiCodeRepository;
import com.icetea.MonStu.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Aspect
@RequiredArgsConstructor
@Component
public class scheduledVerifiCode {

    private final AuthService authService;

    /*  10분마다 실행 (fixedRate = 600000ms)
    * 1 : verified가 true이고, expiresAt가 현재 시간보다 1분 이상 과거인 경우
    * 2 : failedCount가 5 이상이며, failedAt이 현재 시간보다 20분 이상 과거인 경우
    * DB에서 해당 데이터 삭제
    * */
    @Scheduled(fixedRate = 600000)
    public void cleanupVerifiCodes() {
        authService.cleanupVerifiCodes(1,20);
    }

}
