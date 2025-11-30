package com.icetea.MonStu.auth.application;

import com.icetea.MonStu.auth.dto.v2.request.EmailVerifyRequest;
import com.icetea.MonStu.member.dto.v2.request.LoginRequest;
import com.icetea.MonStu.auth.dto.v2.request.VerifyEmailCodeRequest;
import com.icetea.MonStu.auth.dto.v2.response.EmailVerifyResponse;
import com.icetea.MonStu.member.dto.v2.response.MemberSummaryResponse;
import com.icetea.MonStu.member.application.MemberService;
import com.icetea.MonStu.verification.domain.VerifiCode;
import com.icetea.MonStu.shared.manager.EmailManager;
import com.icetea.MonStu.verification.repository.VerifiCodeRepository;
import com.icetea.MonStu.shared.security.details.CustomUserDetails;
import com.icetea.MonStu.shared.security.jwt.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationMng;   // 로그인 인증을 처리
    private final JwtService jwtSvc;                         // JWT를 생성하고 검증
    private final MemberService memberSvc;
    private final VerifiCodeRepository verifiCodeRps;
    private final EmailManager emailMng;

    // 이메일 인증코드 생성 및 전송
    @Transactional
    public EmailVerifyResponse sendVerificationEmailCode(EmailVerifyRequest request) {
        // 코드 생성 및 이메일로 전송
        String code = emailMng.sendVerificationEmailCode(request.email());

        // 이메일에 대해, 기존에 생성된 코드가 있으면 갱신, 없으면 새로 생성
        VerifiCode verifiCode = verifiCodeRps.findByEmail(request.email())
                .orElse(VerifiCode.builder()
                        .email(request.email())
                        .build());

        verifiCode.renewCode(code, 3);

        VerifiCode savedCode = verifiCodeRps.save(verifiCode);
        return EmailVerifyResponse.toDto(savedCode);
    }

    // 이메일 인증코드 인증
    @Transactional
    public void verifyEmailCode(VerifyEmailCodeRequest request) {
        VerifiCode code = verifiCodeRps.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("인증 코드가 만료되었습니다."));

        if (code.getFailedCount() != null && code.getFailedCount() >= 5) {
            throw new IllegalArgumentException("인증 실패 횟수를 초과했습니다. 다시 코드를 발급받아주세요.");
        }

        if (code.isExpired()) {
            throw new IllegalArgumentException("인증 코드가 만료되었습니다. 다시 발급받아주세요.");
        }

        if (!code.verify(request.code())) {
            throw new IllegalArgumentException("인증 코드가 일치하지 않습니다.");
        }
    }


    /* 기능 : 특정 시간마다 불필요한 데이터 삭제
    *  비고 : scheduledVerifiCode.class에서 사용 */
    @Transactional
    public void cleanupVerificationCodes(int expiresTime, int cleanupVerificationCodes) {
        LocalDateTime now = LocalDateTime.now();

        verifiCodeRps.deleteByExpiresAtBefore( now.minusMinutes(expiresTime) );
        verifiCodeRps.deleteByFailedCountGreaterThanEqualAndFailedAtBefore((byte) 5, now.minusMinutes(cleanupVerificationCodes) );
    }

    @Transactional
    public MemberSummaryResponse login(LoginRequest request, HttpServletResponse httpServletResponse) {
        CustomUserDetails userDetail = (CustomUserDetails) authenticationMng.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password())).getPrincipal();

        String token = jwtSvc.generateToken(userDetail);
        jwtSvc.setOption(httpServletResponse, token);

        return memberSvc.getMemberSummaryById(userDetail.getId());
    }
}
