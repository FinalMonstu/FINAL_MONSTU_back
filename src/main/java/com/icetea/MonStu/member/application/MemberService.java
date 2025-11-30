package com.icetea.MonStu.member.application;

import com.icetea.MonStu.member.dto.v2.request.MemberRequest;
import com.icetea.MonStu.auth.dto.v2.response.FindEmailResponse;
import com.icetea.MonStu.member.dto.v2.response.MemberSummaryResponse;
import com.icetea.MonStu.auth.dto.v2.request.FindEmailRequest;
import com.icetea.MonStu.auth.dto.v2.request.ResetPasswordRequest;
import com.icetea.MonStu.member.domain.Member;
import com.icetea.MonStu.member.enums.MemberStatus;
import com.icetea.MonStu.member.dto.v2.request.FilterMemberRequest;
import com.icetea.MonStu.member.dto.v2.request.UpdateMemberRequest;
import com.icetea.MonStu.shared.exception.ConflictException;
import com.icetea.MonStu.shared.exception.EmptyParameterException;
import com.icetea.MonStu.shared.exception.NoSuchElementException;
import com.icetea.MonStu.member.repository.MemberRepository;
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

    @Transactional
    public Long createMember(MemberRequest request) {
        if (memberRps.existsByEmail(request.email()))  throw new ConflictException("이미 사용 중인 이메일입니다.");
        Member savedMember = memberRps.save( MemberMapper.toEntity(request, passwordEncoder) );
        return savedMember.getId();
    }

    // 이메일 중복 확인
    @Transactional(readOnly = true)
    public void validateEmailDuplication(String email) {
        boolean exist = memberRps.existsByEmail(email);
        if(exist) throw new ConflictException("이미 존재하는 이메일입니다");
    }

    @Transactional
    public void resetPassword(ResetPasswordRequest resetPasswordRequest) {
        Member member = memberRps.findByEmail(resetPasswordRequest.email())
                .orElseThrow(()-> new NoSuchElementException("해당 이메일의 회원을 찾을 수 없습니다."));

        String encodedPassword = passwordEncoder.encode(resetPasswordRequest.password());
        member.changePassword(encodedPassword);
    }

    // 이메일 찾기
    @Transactional(readOnly = true)
    public FindEmailResponse findEmail(FindEmailRequest request) {
        return memberRps.findByPhoneNumberAndNickName( request.phoneNumber() ,request.nickName())
                .orElseThrow(()->new NoSuchElementException("일치하는 회원이 없습니다."));
    }

    // 회원 간단 정보 반환
    @Transactional(readOnly = true)
    public MemberSummaryResponse getMemberSummaryById(Long id) {
        return memberRps.findMemberDtoById(id)
                .orElseThrow(()->new NoSuchElementException(null));
    }

    // ADMIN 전용, 회원 정보 수정
    @Transactional
    public void updateMember(Long memberId, UpdateMemberRequest updateMemberRequest) {
        Member member = memberRps.findById(memberId)
                .orElseThrow(()->new NoSuchElementException(null));

        member.updateProfile(
                updateMemberRequest.email(),
                updateMemberRequest.phoneNumber(),
                updateMemberRequest.nickName(),
                updateMemberRequest.country(),
                updateMemberRequest.role(),
                updateMemberRequest.status()
        );
    }

    // 회원 상태 변경
    @Transactional
    public void updateMemberStatus(Long id, MemberStatus status) {
        Member member = memberRps.findById(id)
                .orElseThrow(()->new NoSuchElementException("계정을 찾을 수 없습니다"));
        member.changeStatus(status);
    }


    // status 속성이 'DELETE'인 모든 데이터 삭제
    @Transactional
    public void purgeDeletedMembers() {
        memberRps.deleteAllByStatus(MemberStatus.DELETED);
    }

    // 회원 바로 삭제
    @Transactional
    public void deleteMembers(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new EmptyParameterException("삭제할 회원 ID 목록이 비어있습니다.");
        }
        memberRps.deleteAllById(ids);
    }

    // Pageable과 전달 받은 필터링 값을 이용, 필터링된 멤버 목록 반환
    @Transactional(readOnly = true)
    public Page<MemberSummaryResponse> getPagedFilteredMembers(FilterMemberRequest filterMemberRequest, Pageable pageable) {
        return memberRps.findAllByFilter(filterMemberRequest, pageable);
    }

}
