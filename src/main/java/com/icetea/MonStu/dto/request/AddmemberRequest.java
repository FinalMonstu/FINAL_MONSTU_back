package com.icetea.MonStu.dto.request;

import com.icetea.MonStu.dto.MemberRequest;
import com.icetea.MonStu.enums.CountryCode;
import com.icetea.MonStu.enums.MemberRole;
import com.icetea.MonStu.enums.MemberStatus;

public record AddmemberRequest (
        String email,
        String password,
        String nickName,
        CountryCode country,
        String phoneNumber,
        MemberStatus status,
        MemberRole role
) implements MemberRequest { }
