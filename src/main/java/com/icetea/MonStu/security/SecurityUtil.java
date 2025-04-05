package com.icetea.MonStu.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtil {

    //로그인 한 유저의 username(email) 반환
    public static String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    // 현재 로그인한 유저의 CustomUserDetails 객체 반환
    public static CustomUserDetails getCurrentUserDetails() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof CustomUserDetails) {
            return (CustomUserDetails) auth.getPrincipal();
        }
        return null;
    }

    // 현재 로그인한 유저의 nickname 반환
    public static String getCurrentNickname() {
        CustomUserDetails userDetails = getCurrentUserDetails();
        return userDetails != null ? userDetails.getNickname() : null;
    }

    // 현재 로그인한 유저의 role 반환
    public static String getCurrentUserRole() {
        CustomUserDetails userDetails = getCurrentUserDetails();
        return userDetails != null ? userDetails.getRole() : null;
    }

    // 현재 로그인한 유저의 email 반환 (username == email인 경우)
    public static String getCurrentEmail() {
        CustomUserDetails userDetails = getCurrentUserDetails();
        return userDetails != null ? userDetails.getEmail() : null;
    }
}
