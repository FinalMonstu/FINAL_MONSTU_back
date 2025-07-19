package com.icetea.MonStu.api.v1.dto.request.member;

import com.icetea.MonStu.enums.CountryCode;
import com.icetea.MonStu.enums.MemberRole;
import com.icetea.MonStu.enums.MemberStatus;

import java.util.Date;

public record FilterMemberRequest(
        String email,
        String nickname,

        CountryCode countryCode,
        MemberRole role,
        MemberStatus status,

        String dateOption,
        Date dateStart,
        Date dateEnd


) { }
