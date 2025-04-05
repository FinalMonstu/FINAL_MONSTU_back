package com.icetea.MonStu.service;

import com.icetea.MonStu.dto.request.SignUpRequest;
import com.icetea.MonStu.entity.Member;
import com.icetea.MonStu.enums.MemberRole;
import com.icetea.MonStu.enums.MemberStatus;
import com.icetea.MonStu.exception.ConflictException;
import com.icetea.MonStu.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

//    public MemberService(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }

    // 회원가입
    public void signup(SignUpRequest request) {
        if (memberRepository.existsByEmail(request.email())) { throw new ConflictException("이미 사용 중인 이메일입니다."); }
        Member member = Member.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .nickName(request.nickName())
                .phoneNumber(request.phoneNumber())
                .createdAt(new Date())
                .updatedAt(new Date())
                .status(MemberStatus.ACTIVE)
                .role(MemberRole.MEMBER)
                .countryCode(request.countryCode())
                .build();
        memberRepository.save(member);
    }
}
