package com.icetea.MonStu.dto.auth;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LoginDTO {

    private String email;
    private String password;
}
