package com.icetea.MonStu.dto.request;

import com.icetea.MonStu.dto.FilterRequest;
import com.icetea.MonStu.enums.CountryCode;
import com.icetea.MonStu.enums.MemberRole;
import com.icetea.MonStu.enums.MemberStatus;

import java.util.Date;

public record MemberFilterRequest(
        String email,
        String nickname,

        CountryCode countryCode,
        MemberRole role,
        MemberStatus status,

        String dateOption,
        Date dateStart,
        Date dateEnd


) implements FilterRequest { }
