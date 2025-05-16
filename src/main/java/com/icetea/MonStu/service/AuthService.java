package com.icetea.MonStu.service;

import com.icetea.MonStu.dto.request.auth.SendEmailCodeRequest;
import com.icetea.MonStu.dto.request.auth.VerifyEmailCodeRequest;
import com.icetea.MonStu.dto.request.auth.LoginRequest;
import com.icetea.MonStu.dto.response.auth.VerifiCodeResponse;
import com.icetea.MonStu.dto.response.auth.LiteMemberRespnse;
import com.icetea.MonStu.entity.VerifiCode;
import com.icetea.MonStu.manager.EmailManager;
import com.icetea.MonStu.repository.VerifiCodeRepository;
import com.icetea.MonStu.security.CustomUserDetails;
import com.icetea.MonStu.security.JwtService;
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
    private final AuthenticationManager authenticationManager;   // 로그인 인증을 처리
    private final JwtService jwtService;                         // JWT를 생성하고 검증
    private final MemberService memberService;
    private final VerifiCodeRepository verifiCodeRps;
    private final EmailManager emailManager;

    /* 기능 : 이메일 인증 코드 전송
     * 비고 : 인증 코드 재전송 시 전달 받은 ID로 DB에 새로운 인증정보 삽입 (새로운 행 생성 X),
     * 인증 제한 시간 : 3분 */
    @Transactional
    public VerifiCodeResponse sendEmailCode(SendEmailCodeRequest request) {
        String code = emailManager.sendEmailCode(request.email());
        VerifiCode entity = request.toEntity(code);
        VerifiCode savedCode = verifiCodeRps.save(entity);
        return VerifiCodeResponse.toDto(savedCode);
    }

    // 이메일 인증코드 인증
    @Transactional
    public boolean verifyEmailCode(VerifyEmailCodeRequest request) {
        VerifiCode vc = verifiCodeRps.findById(request.id())
                .orElseThrow(() -> new IllegalArgumentException("인증 코드가 만료되었습니다."));
        return vc.verify(request.code());
    }


    /* 기능 : 특정 시간마다 불필요한 데이터 삭제
    *  비고 : scheduledVerifiCode.class에서 사용 */
    @Transactional
    public void cleanupVerifiCodes(int expiresTime, int failedTime) {
        LocalDateTime now = LocalDateTime.now();

        verifiCodeRps.deleteByExpiresAtBefore( now.minusMinutes(expiresTime) );
        verifiCodeRps.deleteByFailedCountGreaterThanEqualAndFailedAtBefore((byte) 5, now.minusMinutes(failedTime) );
    }

    @Transactional
    public LiteMemberRespnse login(LoginRequest request, HttpServletResponse response) {
        CustomUserDetails ud = (CustomUserDetails) authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password())).getPrincipal();

        String token = jwtService.generateToken(ud);
        jwtService.setOption(response, token);

        return memberService.getLiteById(ud.getId());
    }
}
