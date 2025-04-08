package com.icetea.MonStu.dto.request;

public record VerifiCodeRequest(
        Long id,
        String email,
        String code
) { }
