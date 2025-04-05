package com.icetea.MonStu.dto.request;

import com.icetea.MonStu.enums.CountryCode;

public record SignUpRequest(
        String email,
        String password,
        String nickName,
        CountryCode countryCode,
        String phoneNumber
) {}
