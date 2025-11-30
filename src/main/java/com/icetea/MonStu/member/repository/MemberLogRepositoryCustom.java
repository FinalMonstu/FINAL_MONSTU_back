package com.icetea.MonStu.member.repository;

import com.icetea.MonStu.member.enums.MemberStatus;

public interface MemberLogRepositoryCustom {

    long nullifyLogsByMemberStatus(MemberStatus status);

}
