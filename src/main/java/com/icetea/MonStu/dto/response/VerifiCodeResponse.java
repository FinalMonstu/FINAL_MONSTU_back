package com.icetea.MonStu.dto.response;

public record VerifiCodeResponse(
        Long id,
        String email,
        String message
) { }
