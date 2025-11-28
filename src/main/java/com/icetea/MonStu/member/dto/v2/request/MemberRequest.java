package com.icetea.MonStu.member.dto.v2.request;

import com.icetea.MonStu.member.enums.CountryCode;
import com.icetea.MonStu.member.enums.MemberRole;
import com.icetea.MonStu.member.enums.MemberStatus;

public interface MemberRequest {
    String      email();
    String      nickName();
    CountryCode country();
    String      phoneNumber();

    default String password()       { return null; }

    default MemberStatus status()   { return null; }
    default MemberRole role()       { return null; }
}
