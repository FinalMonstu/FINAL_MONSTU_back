package com.icetea.MonStu.service;

import com.icetea.MonStu.api.v2.dto.MemberRequest;
import com.icetea.MonStu.api.v2.dto.request.*;
import com.icetea.MonStu.api.v2.dto.response.AdminMemberResponse;
import com.icetea.MonStu.api.v2.dto.response.FindEmailResponse;
import com.icetea.MonStu.api.v2.dto.response.MemberProfileResponse;
import com.icetea.MonStu.api.v2.mapper.MemberMapper;
import com.icetea.MonStu.entity.Member;
import com.icetea.MonStu.enums.MemberStatus;
import com.icetea.MonStu.exception.ConflictException;
import com.icetea.MonStu.exception.EmptyParameterException;
import com.icetea.MonStu.exception.NoSuchElementException;
import com.icetea.MonStu.manager.FilterPredicateManager;
import com.icetea.MonStu.repository.MemberRepository;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRps;
    private final PasswordEncoder passwordEncoder;

    // 회원 추가
    @Transactional
    public void createMember(MemberRequest request) {
        if (memberRps.existsByEmail(request.email())) { throw new ConflictException("이미 사용 중인 이메일입니다."); }
        memberRps.save( MemberMapper.toEntity(request, passwordEncoder) );
    }

    // 이메일 중복 확인
    public void existsByEmail(String email) {
        boolean exist = memberRps.existsByEmail(email);
        if(exist) throw new ConflictException("이미 존재하는 이메일입니다.");
    }

    // 비밀번호 재설정
    @Transactional
    public void resetPassword(ResetPasswordRequest resetPasswordRequest) {
        memberRps.findByEmail(resetPasswordRequest.email())
                .map(member -> {
                    MemberMapper.updateFromDto(member, resetPasswordRequest, passwordEncoder);
                    return member;
                })
                .orElseThrow(()-> new NoSuchElementException(null));
    }

    // 이메일 찾기
    public FindEmailResponse findEmail(FindEmailRequest request) {
        return memberRps.findByPhoneNumberAndNickName( request.phoneNumber() ,request.nickName())
                .map(member -> new FindEmailResponse(member.getEmail()))
                .orElseThrow(()->new NoSuchElementException(null));
    }

    // ID 이용, 회원 데이터 반환
    public AdminMemberResponse getById(Long id) {
        return memberRps.findById(id)
                .map(AdminMemberResponse::toDto)
                .orElseThrow(()->new NoSuchElementException(null));
    }

    // 회원 간단 정보 반환
    public MemberProfileResponse getMemberSummaryById(Long id) {
        return memberRps.findById(id)
                .map(MemberProfileResponse::toDto)
                .orElseThrow(()->new NoSuchElementException(null));
    }

    // ADMIN 전용, 회원 정보 수정
    @Transactional
    public void updateMember(Long memberId, UpdateMemberRequest updateMemberRequest) {
        Member member = memberRps.findById(memberId)
                .orElseThrow(()->new NoSuchElementException(null));
        MemberMapper.updateFromDto(member,updateMemberRequest);
    }

    // Id 이용, 회원 비활성화
    @Transactional
    public void updateStatus(Long id,MemberStatus status) {
        Member member = memberRps.findById(id)
                .orElseThrow(()->new NoSuchElementException("계정을 찾을 수 없습니다"));
        member.setStatus(status);
    }

    // ID 목록 이용, 여러 회원 비활성화
    @Transactional
    public void deactivateAll(List<Long> ids) {
        memberRps.updateStatusByIds(ids,MemberStatus.DELETED);
    }

    // status 속성이 'DELETE'인 모든 데이터 삭제
    @Transactional
    public void purgeDeletedMembers() {
        memberRps.deleteAllByStatus();
    }

    // 단일/다중 회원 바로 삭제
    @Transactional
    public void deleteMembers(List<Long> ids) {
        if (ids == null || ids.isEmpty())  throw new EmptyParameterException(null);
        memberRps.deleteAllById(ids);
    }

    // Pageable과 전달 받은 필터링 값을 이용, 필터링된 멤버 목록 반환
    public Page<AdminMemberResponse> filterMembers(FilterMemberRequest filterMemberRequest, Pageable pageable) {
        Predicate predicate = FilterPredicateManager.buildMembersFilterPredicate(filterMemberRequest);
        return memberRps.findAll(predicate, pageable)
            .map(AdminMemberResponse::toDto);
    }

}
