package com.icetea.MonStu.member.repository;

import com.icetea.MonStu.member.dto.v2.request.FilterMemberRequest;
import com.icetea.MonStu.member.dto.v2.response.MemberSummaryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface MemberRepositoryCustom {

    Page<MemberSummaryResponse> findAllByFilter(FilterMemberRequest filterDTO, Pageable pageable);

    Optional<MemberSummaryResponse> findMemberDtoById(Long memberId);

}
