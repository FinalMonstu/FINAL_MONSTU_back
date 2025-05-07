package com.icetea.MonStu.security;

import com.icetea.MonStu.entity.Member;
import com.icetea.MonStu.repository.MemberRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRPS;

    public CustomUserDetailsService(MemberRepository memberRepository) {
        this.memberRPS = memberRepository;
    }

    @Override   //Spring Security에서 인증할 때 사용하는 UserDetails 객체를 생성
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRPS.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new CustomUserDetails(member);
    }
}

