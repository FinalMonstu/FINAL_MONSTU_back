package com.icetea.MonStu.member.dto.v2.request;

import com.icetea.MonStu.member.enums.CountryCode;
import com.icetea.MonStu.member.enums.MemberDateOption;
import com.icetea.MonStu.member.enums.MemberRole;
import com.icetea.MonStu.member.enums.MemberStatus;

import java.time.LocalDate;

public record FilterMemberRequest(

        String email,
        String nickname,

        CountryCode countryCode,
        MemberRole role,
        MemberStatus status,

        MemberDateOption dateOption,
        LocalDate dateStart,
        LocalDate dateEnd
) { }
