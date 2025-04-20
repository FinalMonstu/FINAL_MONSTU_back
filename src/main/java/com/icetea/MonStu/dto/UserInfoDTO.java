package com.icetea.MonStu.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.icetea.MonStu.enums.MemberRole;
import lombok.*;

@Getter
@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY) //null,빈값 제외
public class UserInfoDTO {
    private String email;
    private MemberRole role;
}
