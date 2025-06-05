package com.icetea.MonStu.security;

import com.icetea.MonStu.entity.Member;
import com.icetea.MonStu.enums.MemberStatus;
import com.icetea.MonStu.exception.UnauthorizedException;
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
        Member member = memberRPS.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("정보가 존재하지 않습니다"));
        if (member.getStatus() == MemberStatus.BANNED) { throw new UnauthorizedException("이용할 수 없는 정보입니다"); }
        return new CustomUserDetails(member);
    }
}

