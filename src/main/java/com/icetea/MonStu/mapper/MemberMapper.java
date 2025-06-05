package com.icetea.MonStu.mapper;

import com.icetea.MonStu.dto.MemberRequest;
import com.icetea.MonStu.dto.request.auth.ResetPasswordRequest;
import com.icetea.MonStu.dto.request.member.UpdateMemberRequest;
import com.icetea.MonStu.entity.Member;
import com.icetea.MonStu.enums.MemberRole;
import com.icetea.MonStu.enums.MemberStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;

public final class MemberMapper {
    private MemberMapper() {}

    public static Member toEntity(MemberRequest request, PasswordEncoder encoder){
        Member.MemberBuilder b = Member.builder()
                .email(request.email())
                .password(encoder.encode(request.password()))
                .nickName(request.nickName())
                .phoneNumber(request.phoneNumber()) // 암호화된 값 저장
                .createdAt(new Date())
                .updatedAt(new Date())
                .countryCode(request.country())
                .status(request.status() != null ? request.status() : MemberStatus.ACTIVE)
                .role(request.role()   != null ? request.role()   : MemberRole.MEMBER);
        return b.build();
    }

    public static void updateFromDto(Member m,ResetPasswordRequest req, PasswordEncoder encoder){
        m.setUpdatedAt(new Date());
        m.setPassword(encoder.encode(req.password()));
    }

    public static void updateFromDto(Member m, UpdateMemberRequest req){
        m.setEmail(req.email());
        m.setNickName(req.nickName());
        m.setPhoneNumber(req.phoneNumber());
        m.setUpdatedAt(new Date());
        m.setStatus(req.status());
        m.setRole(req.role());
        m.setCountryCode(req.country());
    }
}
