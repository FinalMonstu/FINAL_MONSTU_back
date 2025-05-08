package com.icetea.MonStu.dto;

import com.icetea.MonStu.enums.CountryCode;
import com.icetea.MonStu.enums.MemberRole;
import com.icetea.MonStu.enums.MemberStatus;

import java.util.Date;

public interface MemberRequest {
    String email();
    String nickName();
    CountryCode country();
    String phoneNumber();

    default String password() { return null; }

    default MemberStatus status() { return null; }
    default MemberRole role() { return null; }
}
