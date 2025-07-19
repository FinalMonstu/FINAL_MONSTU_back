package com.icetea.MonStu.service;

import com.icetea.MonStu.api.v1.dto.MemberRequest;
import com.icetea.MonStu.api.v1.dto.request.member.MemberFilterRequest;
import com.icetea.MonStu.api.v1.dto.request.member.UpdateMemberRequest;
import com.icetea.MonStu.api.v1.dto.response.member.MemberResponse;
import com.icetea.MonStu.api.v1.dto.request.auth.FindEmailRequest;
import com.icetea.MonStu.api.v1.dto.request.auth.ResetPasswordRequest;
import com.icetea.MonStu.api.v1.dto.response.auth.LiteMemberResponse;
import com.icetea.MonStu.api.v1.dto.response.auth.EmailFindResponse;
import com.icetea.MonStu.entity.Member;
import com.icetea.MonStu.enums.MemberStatus;
import com.icetea.MonStu.exception.ConflictException;
import com.icetea.MonStu.exception.NoSuchElementException;
import com.icetea.MonStu.manager.FilterPredicateManager;
import com.icetea.MonStu.api.v1.mapper.MemberMapper;
import com.icetea.MonStu.repository.MemberRepository;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
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
    public void register(MemberRequest request) {
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
    public void resetPassword(ResetPasswordRequest request) {
        memberRps.findByEmail(request.email())
                .map(member -> {
                    MemberMapper.updateFromDto(member, request, passwordEncoder);
                    return member;
                })
                .orElseThrow(()-> new NoSuchElementException(null));
    }

    // 이메일 찾기
    public EmailFindResponse findEmail(FindEmailRequest request) {
        return memberRps.findByPhoneNumberAndNickName( request.phoneNumber() ,request.nickName())
                .map(member -> new EmailFindResponse(member.getEmail()))
                .orElseThrow(()->new NoSuchElementException(null));
    }

    // ID 이용, 회원 데이터 반환
    public MemberResponse getById(Long id) {
        return memberRps.findById(id)
                .map(MemberResponse::toDto)
                .orElseThrow(()->new NoSuchElementException(null));
    }

    // 회원 간단 정보 반환
    public LiteMemberResponse getLiteById(Long id) {
        return memberRps.findById(id)
                .map(LiteMemberResponse::toDto)
                .orElseThrow(()->new NoSuchElementException(null));
    }

    // ADMIN 전용, 회원 정보 수정
    @Transactional
    public void updateMember(UpdateMemberRequest request) {
        Member member = memberRps.findById(request.id())
                .orElseThrow(()->new NoSuchElementException(null));
        MemberMapper.updateFromDto(member,request);
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


    // Pageable과 전달 받은 필터링 값을 이용, 필터링된 멤버 목록 반환
    public Page<MemberResponse> filterMembers(MemberFilterRequest filterDTO, Pageable pageable) {
        Predicate predicate = FilterPredicateManager.buildMembersFilterPredicate(filterDTO);
        return memberRps.findAll(predicate, pageable)
            .map(MemberResponse::toDto);
    }

}
