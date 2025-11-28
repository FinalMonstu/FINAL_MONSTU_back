package com.icetea.MonStu.member.application;

import com.icetea.MonStu.member.dto.v2.request.MemberRequest;
import com.icetea.MonStu.auth.dto.v2.request.ResetPasswordRequest;
import com.icetea.MonStu.member.dto.v2.request.UpdateMemberRequest;
import com.icetea.MonStu.member.domain.Member;
import com.icetea.MonStu.member.enums.MemberRole;
import com.icetea.MonStu.member.enums.MemberStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

public final class MemberMapper {
    private MemberMapper() {}

    public static Member toEntity(MemberRequest request, PasswordEncoder encoder){
        Member.MemberBuilder b = Member.builder()
                .email(request.email())
                .password(encoder.encode(request.password()))
                .nickName(request.nickName())
                .phoneNumber(request.phoneNumber()) // 암호화된 값 저장
                .createdAt(LocalDate.now())
                .updatedAt(LocalDate.now())
                .countryCode(request.country())
                .status(request.status() != null ? request.status() : MemberStatus.ACTIVE)
                .role(request.role()   != null ? request.role()   : MemberRole.MEMBER);
        return b.build();
    }

    public static void updateFromDto(Member m, ResetPasswordRequest req, PasswordEncoder encoder){
        m.setUpdatedAt(LocalDate.now());
        m.setPassword(encoder.encode(req.password()));
    }

    public static void updateFromDto(Member m, UpdateMemberRequest req){
        m.setEmail(req.email());
        m.setNickName(req.nickName());
        m.setPhoneNumber(req.phoneNumber());
        m.setUpdatedAt(LocalDate.now());
        m.setStatus(req.status());
        m.setRole(req.role());
        m.setCountryCode(req.country());
    }
}
