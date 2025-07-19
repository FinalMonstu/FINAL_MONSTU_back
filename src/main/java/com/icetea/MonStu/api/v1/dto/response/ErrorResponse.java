package com.icetea.MonStu.api.v1.dto.response;

public record ErrorResponse(
        String error,
        String message
) {}
