package com.icetea.MonStu.dto.request;

public record FindEmailRequest(
        String phoneNumber,
        String nickName
) { }
