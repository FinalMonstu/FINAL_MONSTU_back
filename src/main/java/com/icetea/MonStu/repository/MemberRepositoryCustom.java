package com.icetea.MonStu.repository;

import com.icetea.MonStu.api.v2.dto.request.FilterMemberRequest;
import com.icetea.MonStu.api.v2.dto.response.MemberSummaryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface MemberRepositoryCustom {

    Page<MemberSummaryResponse> findAllByFilter(FilterMemberRequest filterDTO, Pageable pageable);

    Optional<MemberSummaryResponse> findMemberDtoById(Long memberId);

}
