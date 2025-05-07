package com.icetea.MonStu.service;

import com.icetea.MonStu.dto.request.SendEmailCodeRequest;
import com.icetea.MonStu.dto.request.VerifyEmailCodeRequest;
import com.icetea.MonStu.dto.response.VerifiCodeResponse;
import com.icetea.MonStu.entity.VerifiCode;
import com.icetea.MonStu.exception.UnauthorizedException;
import com.icetea.MonStu.manager.EmailManager;
import com.icetea.MonStu.repository.VerifiCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final VerifiCodeRepository verifiCodeRps;

    private final EmailManager emailManager;


    /* 기능 : 이메일 인증 코드 전송
     * 비고 : 인증 코드 재전송 시 전달 받은 ID로 DB에 새로운 인증정보 삽입 (새로운 행 생성 X),
     * 인증 제한 시간 : 3분 */
    @Transactional
    public VerifiCodeResponse sendEmailCode(SendEmailCodeRequest request) {
        String code = emailManager.sendEmailPass(request.email());   // 인증 이메일 전송, 인증코드 반환
        if (!StringUtils.hasText(code)) throw new RuntimeException("이메일 전송에 실패했습니다.");  //코드 생성 에러 처리

        VerifiCode verifiCode = VerifiCode.builder()
                .email(request.email())
                .code(code)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(3))
                .verified(false)
                .failedCount((byte) 0)
                .build();

        if (request.id() != null) verifiCode.setId(request.id());  //이미 생성된 행이 있다면 해당 행에 정보 삽입

        VerifiCode savedCode = verifiCodeRps.save(verifiCode);
        return VerifiCodeResponse.mapper(savedCode);
    }


    @Transactional
    public boolean verifyEmailCode(VerifyEmailCodeRequest request) {
        VerifiCode vc = verifiCodeRps.findById(request.id())
                .orElseThrow(() -> new IllegalArgumentException("인증 코드가 만료되었습니다."));

        return vc.verify(request.code());
    }


    /* 기능 : 특정 시간마다 불필요한 데이터 삭제
    *  비고 : scheduledVerifiCode.class에서 사용 중*/
    @Transactional
    public void cleanupVerifiCodes(int expiresTime, int failedTime) {
        LocalDateTime now = LocalDateTime.now();

        verifiCodeRps.deleteByExpiresAtBefore( now.minusMinutes(expiresTime) );
        verifiCodeRps.deleteByFailedCountGreaterThanEqualAndFailedAtBefore((byte) 5, now.minusMinutes(failedTime) );
    }

}
