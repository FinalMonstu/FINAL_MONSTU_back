package com.icetea.MonStu.api.v1.dto.request.auth;

import lombok.Builder;

@Builder
public record LoginRequest(

        String email,

        String password
) {}
