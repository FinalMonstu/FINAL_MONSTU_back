package com.icetea.MonStu.service;

import com.icetea.MonStu.dto.request.EmailPasswordRequest;
import com.icetea.MonStu.dto.request.FindEmailRequest;
import com.icetea.MonStu.dto.request.SignUpRequest;
import com.icetea.MonStu.dto.response.FindEmailResponse;
import com.icetea.MonStu.entity.Member;
import com.icetea.MonStu.enums.MemberRole;
import com.icetea.MonStu.enums.MemberStatus;
import com.icetea.MonStu.exception.ConflictException;
import com.icetea.MonStu.exception.NoSuchElementException;
import com.icetea.MonStu.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRps;

    private final PasswordEncoder passwordEncoder;


    // 회원가입
    @Transactional
    public void signup(SignUpRequest request) {
        if (memberRps.existsByEmail(request.email())) { throw new ConflictException("이미 사용 중인 이메일입니다."); }
        Member member = Member.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .nickName(request.nickName())
                .phoneNumber(passwordEncoder.encode(request.phoneNumber()))
                .createdAt(new Date())
                .updatedAt(new Date())
                .status(MemberStatus.ACTIVE)
                .role(MemberRole.MEMBER)
                .countryCode(request.country())
                .build();
        memberRps.save(member);
    }

    public Boolean existsByEmail(String email) {
        return memberRps.existsByEmail(email);
    }

    // 비밀번호 재설정
    @Transactional
    public void resetPassword(EmailPasswordRequest request) {
        int success = memberRps.updatePasswordByEmail(request.email(), passwordEncoder.encode(request.password()) );
        if (success == 0)  throw new IllegalArgumentException("해당 이메일의 회원이 존재하지 않습니다.");
    }

    // 이메일 찾기
    public FindEmailResponse findEmail(FindEmailRequest request) {
        Member member = memberRps.findByPhoneNumberAndNickName(request.phoneNumber(),request.nickName())
                .orElseThrow(()->new NoSuchElementException("해당 전화번호와 닉네임을 가진 회원이 존재하지 않습니다."));
        return new FindEmailResponse( member.getEmail() );
    }
}
