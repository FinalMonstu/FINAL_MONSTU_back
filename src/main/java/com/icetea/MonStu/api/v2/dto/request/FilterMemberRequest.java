package com.icetea.MonStu.api.v2.dto.request;

import com.icetea.MonStu.enums.CountryCode;
import com.icetea.MonStu.enums.MemberRole;
import com.icetea.MonStu.enums.MemberStatus;

import java.time.LocalDate;
import java.util.Date;

public record FilterMemberRequest(

        String email,
        String nickname,

        CountryCode countryCode,
        MemberRole role,
        MemberStatus status,

        String dateOption,
        LocalDate dateStart,
        LocalDate dateEnd
) { }
