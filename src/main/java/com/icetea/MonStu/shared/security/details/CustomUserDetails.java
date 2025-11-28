package com.icetea.MonStu.shared.security.details;

import com.icetea.MonStu.member.domain.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    private final Member member;

    public CustomUserDetails(Member member) {
        this.member = member;
    }

    public Long getId() { return member.getId(); }

    public String getEmail() {
        return member.getEmail();
    }

    public String getNickname() {
        return member.getNickName();
    }

    public String getRole() {
        return member.getRole().name();
    }

    public String getCountryCode() {
        return member.getCountryCode().name();
    }

    @Override
    public String getUsername() {
        return member.getEmail();  // email을 username으로 사용
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + member.getRole().name()));
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}

