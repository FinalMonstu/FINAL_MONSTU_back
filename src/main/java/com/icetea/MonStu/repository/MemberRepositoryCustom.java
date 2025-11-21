package com.icetea.MonStu.repository;

import com.icetea.MonStu.api.v2.dto.request.FilterMemberRequest;
import com.icetea.MonStu.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberRepositoryCustom {

    Page<Member> findAllByFilter(FilterMemberRequest filterDTO, Pageable pageable);

}
