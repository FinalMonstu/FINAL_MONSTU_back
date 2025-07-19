package com.icetea.MonStu.api.v2.dto.request;

import lombok.Builder;

@Builder
public record LoginRequest( String email, String password ) {}
