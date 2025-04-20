package com.icetea.MonStu.service;

import com.icetea.MonStu.dto.request.*;
import com.icetea.MonStu.dto.response.FindEmailResponse;
import com.icetea.MonStu.entity.Member;
import com.icetea.MonStu.enums.MemberRole;
import com.icetea.MonStu.enums.MemberStatus;
import com.icetea.MonStu.exception.ConflictException;
import com.icetea.MonStu.exception.NoSuchElementException;
import com.icetea.MonStu.repository.MemberRepository;
import com.icetea.MonStu.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRps;

    private final UserDetailsService userDetailsService;

    private final PasswordEncoder passwordEncoder;
    private final StringEncryptor jasypt;

    // 회원가입
    @Transactional
    public void signup(SignUpRequest request) {
        if (memberRps.existsByEmail(request.email())) { throw new ConflictException("이미 사용 중인 이메일입니다."); }
        Member member = Member.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .nickName(request.nickName())
                .phoneNumber(jasypt.encrypt(request.phoneNumber()))
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
        Member member = memberRps.findByPhoneNumberAndNickName( jasypt.encrypt(request.phoneNumber()) ,request.nickName())
                .orElseThrow(()->new NoSuchElementException("해당 전화번호와 닉네임을 가진 회원이 존재하지 않습니다."));
        return new FindEmailResponse( member.getEmail() );
    }

    @Transactional
    public void deleteMembers() {
        System.out.println("DELETE MEMBERS");
        memberRps.deleteAllByStatus();
    }

    @Transactional
    public void signout() {
        String email = SecurityUtil.getCurrentUsername();
        System.out.println("Sign Out Email: " + email);
        int updated = memberRps.updateStatusByEmail(email, MemberStatus.DELETED);
        if (updated == 0) throw new NoSuchElementException("탈퇴할 계정을 찾을 수 없습니다: " + email);
    }

}
