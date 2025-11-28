package com.icetea.MonStu.shared.security.details;

import com.icetea.MonStu.member.domain.Member;
import com.icetea.MonStu.member.enums.MemberStatus;
import com.icetea.MonStu.shared.exception.UnauthorizedException;
import com.icetea.MonStu.member.repository.MemberRepository;
import com.icetea.MonStu.shared.cache.CustomTTLCircleCache;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRPS;
    private final CustomTTLCircleCache<String, CustomUserDetails> customUserDetailsCache;

    public CustomUserDetailsService(MemberRepository memberRepository,
                                    CustomTTLCircleCache<String, CustomUserDetails> customUserDetailsCache) {
        this.memberRPS = memberRepository;
        this.customUserDetailsCache = customUserDetailsCache;
    }


    @Override   //Spring Security에서 인증할 때 사용하는 UserDetails 객체를 생성
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        CustomUserDetails cached = customUserDetailsCache.getValue(email);
        if (cached != null) { return cached; }

        Member member = memberRPS.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("정보가 존재하지 않습니다"));
        if (member.getStatus() == MemberStatus.BANNED) { throw new UnauthorizedException("이용할 수 없는 정보입니다"); }

        CustomUserDetails userDetails = new CustomUserDetails(member);

        customUserDetailsCache.putValue(email, userDetails);

        return userDetails;
    }
}

