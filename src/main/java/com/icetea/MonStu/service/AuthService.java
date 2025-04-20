package com.icetea.MonStu.service;

import com.icetea.MonStu.dto.request.EmailPasswordRequest;
import com.icetea.MonStu.dto.request.VerifiCodeRequest;
import com.icetea.MonStu.dto.response.VerifiCodeResponse;
import com.icetea.MonStu.entity.VerifiCode;
import com.icetea.MonStu.exception.UnauthorizedException;
import com.icetea.MonStu.manager.EmailManager;
import com.icetea.MonStu.repository.MemberRepository;
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
    private final MemberRepository memberRps;


    // 이메일 인증 코드 전송
    @Transactional
    public VerifiCodeResponse sendEmailCode(VerifiCodeRequest request) {
        String code = emailManager.sendEmailPass(request.email());
        if (!StringUtils.hasText(code)) throw new RuntimeException("이메일 전송에 실패했습니다.");  //코드 생성 에러 처리
        VerifiCode verifiCode = VerifiCode.builder()
                .email(request.email())
                .code(code)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(1))
                .verified(false)
                .failedCount((byte)0)
                .build();
        if(request.id()!=null) verifiCode.setId(request.id());  //이미 생성된 컬럼이 있다면
        VerifiCode addedVerifiCode = verifiCodeRps.save(verifiCode);
        return  new VerifiCodeResponse(
                addedVerifiCode.getId(),
                addedVerifiCode.getEmail(),
                "이메일을 확인해주세요.");
    }

    @Transactional
    public void verifyEmailCode(VerifiCodeRequest request) {
        if(request.id() < 0 || !StringUtils.hasText(request.code())) throw new IllegalArgumentException("필수 값이 누락되었습니다.");
        VerifiCode verifiCode = verifiCodeRps.findByIdAndEmailAndCodeAndExpiresAtGreaterThanEqual(
                request.id(),
                request.email(),
                request.code(),
                LocalDateTime.now()
        ).orElseThrow(() -> {
            increaseFailedCount(request.id()); // 인증 실패 시
            return new UnauthorizedException("인증 코드가 일치하지 않거나 만료되었습니다.");
        });
        verifiCode.setVerified(true); // 인증 성공 시 verified = true
        verifiCodeRps.save(verifiCode);
    }

    // 특정 시간마다 불필요한 데이터 삭제
    @Transactional
    public void cleanupVerifiCodes(int expiresTime, int failedTime) {
        LocalDateTime now = LocalDateTime.now();

        verifiCodeRps.deleteByExpiresAtBefore( now.minusMinutes(expiresTime) );
        verifiCodeRps.deleteByFailedCountGreaterThanEqualAndFailedAtBefore((byte) 5, now.minusMinutes(failedTime) );
    }

    // 인증 실패 시
    private void increaseFailedCount(Long id) {
        VerifiCode verifiCode = verifiCodeRps.findById(id).orElseThrow(() -> new IllegalArgumentException("에러가 발생했습니다.") );
        byte current = verifiCode.getFailedCount() == null ? 0 : verifiCode.getFailedCount();
        verifiCode.setFailedCount( (byte) (current + 1) );
        verifiCode.setFailedAt(LocalDateTime.now());
        verifiCodeRps.save(verifiCode);
    }

}
