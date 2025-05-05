package com.icetea.MonStu.service;

import com.icetea.MonStu.dto.FilterRequest;
import com.icetea.MonStu.dto.MemberRequest;
import com.icetea.MonStu.dto.common.MemberDTO;
import com.icetea.MonStu.dto.common.MemberInfoDTO;
import com.icetea.MonStu.dto.common.MemberLogDTO;
import com.icetea.MonStu.dto.request.*;
import com.icetea.MonStu.dto.response.FindEmailResponse;
import com.icetea.MonStu.dto.response.PostLiteResponse;
import com.icetea.MonStu.entity.Member;
import com.icetea.MonStu.entity.QMember;
import com.icetea.MonStu.entity.log.MemberLog;
import com.icetea.MonStu.entity.log.QMemberLog;
import com.icetea.MonStu.enums.MemberRole;
import com.icetea.MonStu.enums.MemberStatus;
import com.icetea.MonStu.exception.ConflictException;
import com.icetea.MonStu.exception.NoSuchElementException;
import com.icetea.MonStu.repository.MemberRepository;
import com.icetea.MonStu.repository.log.MemberLogRepository;
import com.icetea.MonStu.security.SecurityUtil;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.jasypt.encryption.StringEncryptor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

import static com.querydsl.core.types.dsl.Expressions.allOf;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRps;

    private final UserDetailsService userDetailsService;

    private final PasswordEncoder passwordEncoder;
    private final StringEncryptor jasypt;
    private final ModelMapper modelMapper;
    private final MemberLogRepository memberLogRps;

    // 회원가입
    @Transactional
    public void signup(MemberRequest request) {
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
        if(request.createdAt()!=null) member.setCreatedAt(request.createdAt());
        if(request.status()!=null) member.setStatus( request.status() );
        if(request.role()!=null) member.setRole( request.role() );

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

    @Transactional
    public void signoutMembers(List<Long> ids) {
        memberRps.updateStatusById(ids,MemberStatus.DELETED);
    }

    // Update Status to 'DELETE' by id
    @Transactional
    public void deleteMember(Long id) {
        Member member = memberRps.findById(id).orElseThrow(()->new NoSuchElementException(null));
        member.setStatus(MemberStatus.DELETED);
    }

    @Transactional
    public MemberDTO getMember(Long id) {
        return memberRps.findById(id)
                .map(MemberDTO::mapper)
                .orElseThrow(()->new NoSuchElementException(null));
//        MemberDTO memberDTO = modelMapper.map(member, MemberDTO.class);
    }

    // 회원의 id,email,nickname,role,countryCode를 가진 DTO를 반환
    public MemberInfoDTO getMemberInfoByEmail(String email) {
        Member member = memberRps.findByEmail(email).orElseThrow(()->new NoSuchElementException("일치하는 회원을 찾을 수 없습니다"));
        return MemberInfoDTO.builder()
                .id(member.getId())
                .email(member.getEmail())
                .nickName(member.getNickName())
                .role(member.getRole())
                .countryCode(member.getCountryCode())
                .build();
    }

    private Predicate buildMemberPredicate(MemberFilterRequest filterDTO) {
        QMember member = QMember.member;

        BooleanExpression predicate = allOf(
            StringUtils.hasText(filterDTO.email())
                    ? member.email.containsIgnoreCase(filterDTO.email())
                    : null,
            StringUtils.hasText(filterDTO.nickname())
                    ? member.nickName.containsIgnoreCase(filterDTO.nickname())
                    : null,
            filterDTO.countryCode() != null
                    ? member.countryCode.eq(filterDTO.countryCode())
                    : null,
            filterDTO.role() != null
                    ? member.role.eq(filterDTO.role())
                    : null,
            filterDTO.status() != null
                    ? member.status.eq(filterDTO.status())
                    : null
        );

        BooleanBuilder builder = new BooleanBuilder(predicate);
        if (StringUtils.hasText(filterDTO.dateOption()) && filterDTO.dateStart() != null && filterDTO.dateEnd() != null) {
            switch (filterDTO.dateOption()) {
                case "createdAt" -> builder
                        .and(member.createdAt.between(filterDTO.dateStart(), filterDTO.dateEnd()));
                case "updatedAt" -> builder
                        .and(member.updatedAt.between(filterDTO.dateStart(), filterDTO.dateEnd()));
            }
        }
//        return builder.and(memberLog.member.isNotNull());
        return builder;
    }

    public Page<MemberDTO> findMembers(MemberFilterRequest filterDTO, Pageable pageable) {
        Predicate predicate = buildMemberPredicate(filterDTO);
        return memberRps.findAll(predicate, pageable)
                .map(MemberDTO::mapper);
    }
}
