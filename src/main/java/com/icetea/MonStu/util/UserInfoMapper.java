package com.icetea.MonStu.util;

import com.icetea.MonStu.dto.UserInfoDTO;
import com.icetea.MonStu.enums.MemberRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserInfoMapper {

    private UserInfoMapper() { /* static util */ }

    public static UserInfoDTO toDto(UserDetails user) {
        MemberRole role = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)   // ex. "ROLE_MEMBER"
                .map(s -> s.replace("ROLE_", ""))      // "MEMBER"
                .map(MemberRole::valueOf)              // MemberRole.MEMBER
                .findFirst()
                .orElse(MemberRole.GUEST);

        return UserInfoDTO.builder()
                .email(user.getUsername())
                .role(role)
                .build();
    }
}
