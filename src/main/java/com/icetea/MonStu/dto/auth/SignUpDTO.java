package com.icetea.MonStu.dto.auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.icetea.MonStu.enums.CountryCode;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY) //null,빈값 제외
public class SignUpDTO {

    private String email;
    private String password;
    private String nickName;
    private CountryCode countryCode;
    private String phoneNumber;

}
