package com.icetea.MonStu.api.v2.dto.response;

public record ErrorResponse(
        String error,
        String message
) {}
