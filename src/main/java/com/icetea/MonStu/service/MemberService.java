package com.icetea.MonStu.service;

import com.icetea.MonStu.dto.MemberRequest;
import com.icetea.MonStu.dto.common.MemberDTO;
import com.icetea.MonStu.dto.common.MemberLiteInfoDTO;
import com.icetea.MonStu.dto.request.*;
import com.icetea.MonStu.dto.response.EmailFindResponse;
import com.icetea.MonStu.entity.Member;
import com.icetea.MonStu.entity.QMember;
import com.icetea.MonStu.enums.MemberRole;
import com.icetea.MonStu.enums.MemberStatus;
import com.icetea.MonStu.exception.ConflictException;
import com.icetea.MonStu.exception.NoSuchElementException;
import com.icetea.MonStu.manager.FilterPredicateManager;
import com.icetea.MonStu.repository.MemberRepository;
import com.icetea.MonStu.security.SecurityUtil;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
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
    private final PasswordEncoder passwordEncoder;  //비밀번호 인코더

    // 회원가입
    @Transactional
    public void signup(MemberRequest request) {
        if (memberRps.existsByEmail(request.email())) { throw new ConflictException("이미 사용 중인 이메일입니다."); }

        Member member = Member.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .nickName(request.nickName())
                .phoneNumber(request.phoneNumber()) // 암호화된 값 저장
                .createdAt(new Date())
                .updatedAt(new Date())
                .status(MemberStatus.ACTIVE)    // 기본값 'ACTIVE'
                .role(MemberRole.MEMBER)        // 기본값 'MEMBER'
                .countryCode(request.country())
                .build();

        // Admin Page에서 유저 저장 시 사용
        if(request.status()!=null) member.setStatus( request.status() );
        if(request.role()!=null) member.setRole( request.role() );

        memberRps.save(member);
    }

    // 이메일 중복 확인
    public void existsByEmail(String email) {
        boolean exist = memberRps.existsByEmail(email);
        if(exist) throw new ConflictException("이미 존재하는 이메일입니다.");
    }

    // 비밀번호 재설정
    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        Member member = memberRps.findByEmail(request.email())
                .orElseThrow(()-> new NoSuchElementException(null));
        member.setUpdatedAt(new Date());
        member.setPassword(passwordEncoder.encode(request.password()));
    }

    // 이메일 찾기
    public EmailFindResponse findEmail(FindEmailRequest request) {
        Member member = memberRps.findByPhoneNumberAndNickName( request.phoneNumber() ,request.nickName())
                .orElseThrow(()->new NoSuchElementException(null));
        return new EmailFindResponse( member.getEmail() );
    }

    // status 속성이 'DELETE'인 모든 데이터 삭제
    @Transactional
    public void deleteMembers() {
        memberRps.deleteAllByStatus();
    }


    @Transactional
    public void signout() {
        String email = SecurityUtil.getCurrentUsername();
        System.out.println("Sign Out Email: " + email);
        int updated = memberRps.updateStatusByEmail(email, MemberStatus.DELETED);
        if (updated == 0) throw new NoSuchElementException("계정을 찾을 수 없습니다: " + email);
    }

    // ID 목록 이용, 회원 status 속성을 'DELETE'로 변경
    @Transactional
    public void signoutMembers(List<Long> ids) {
        memberRps.updateStatusById(ids,MemberStatus.DELETED);
    }

    // 권한 : ADMIN , 회원 정보 수정
    @Transactional
    public void updateMember(UpdateMemberRequest request) {
        Member member = memberRps.findById(request.id())
                .orElseThrow(()->new NoSuchElementException(null));
        member.setEmail(request.email());
        member.setNickName(request.nickName());
        member.setPhoneNumber(request.phoneNumber());
        member.setUpdatedAt(new Date());

        member.setStatus(request.status());
        member.setRole(request.role());
        member.setCountryCode(request.country());
    }

    // Update Status to 'DELETE' by id
    @Transactional
    public void deleteMember(Long id) {
        Member member = memberRps.findById(id).orElseThrow(()->new NoSuchElementException(null));
        member.setStatus(MemberStatus.DELETED);
    }


    // ID 이용, 회원 데이터 반환
    public MemberDTO getMember(Long id) {
        return memberRps.findById(id)
                .map(MemberDTO::mapper)
                .orElseThrow(()->new NoSuchElementException(null));
    }

    // 회원의 id,email,nickname,role,countryCode를 가진 DTO를 반환
    public MemberLiteInfoDTO getMemberLiteInfoByEmail(String email) {
        Member member = memberRps.findByEmail(email).orElseThrow(()->new NoSuchElementException(null));
        return MemberLiteInfoDTO.builder()
                .id(member.getId())
                .email(member.getEmail())
                .nickName(member.getNickName())
                .role(member.getRole())
                .countryCode(member.getCountryCode())
                .build();
    }

    // Pageable과 전달 받은 필터 정보를 이용, 필터링된 멤버 목록 반환
    public Page<MemberDTO> filterMembers(MemberFilterRequest filterDTO, Pageable pageable) {
        Predicate predicate = FilterPredicateManager.buildMembersFilterPredicate(filterDTO);
        return memberRps.findAll(predicate, pageable)
                .map(MemberDTO::mapper);
    }


    public MemberDTO getMyInfo(String email) {
        Member member = memberRps.findByEmail(email).orElseThrow(()->new NoSuchElementException(null));
        return MemberDTO.builder()
                .memberId(member.getId())
                .email(member.getEmail())
                .nickName(member.getNickName())
                .phoneNumber( member.getPhoneNumber())
                .countryCode(member.getCountryCode())
                .build();
    }
}
