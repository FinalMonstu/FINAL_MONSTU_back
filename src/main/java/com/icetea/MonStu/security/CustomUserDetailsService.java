package com.icetea.MonStu.security;

import com.icetea.MonStu.entity.Member;
import com.icetea.MonStu.repository.MemberRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    public CustomUserDetailsService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override   //Spring Security에서 인증할 때 사용하는 UserDetails 객체를 생성
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return org.springframework.security.core.userdetails.User.builder()
                .username(member.getEmail())
                .password(member.getPassword()) // 비밀번호는 반드시 인코딩된 값이어야 함!
                .roles(member.getRole().name())
                .build();
    }
}

