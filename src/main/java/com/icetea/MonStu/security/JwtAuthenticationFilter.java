package com.icetea.MonStu.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
/*
* 모든 요청마다 실행되며, 토큰이 유효한 경우 SecurityContext에 인증 정보를 설정한다.
* SecurityContext는 Spring Security에서 인증된 사용자 정보를 저장하는 공간
* */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");  // 요청 헤더에서 Authorization 값 추출
        final String jwt;
        final String username;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {      // Authorization 헤더가 없거나 Bearer 토큰이 아닌 경우 필터 종료
            filterChain.doFilter(request, response);        //다음 필터(또는 컨트롤러)로 요청을 넘겨주는 코드
            return;
        }

        jwt = authHeader.substring(7);      // Bearer 다음에 오는 실제 JWT 토큰만 추출
        username = jwtService.extractUsername(jwt);     // 토큰에서 사용자 이름(이메일 등) 추출

        // 사용자 정보가 있고 아직 인증되지 않은 경우
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // 토큰이 유효한 경우 인증 객체를 생성하여 SecurityContext에 등록
            if (jwtService.isTokenValid(jwt, userDetails)) {
                // 사용자 정보,자격 증명 (null로 설정,원래는 비밀번호가 들어가는 자리),권한 정보 (예: ROLE_USER, ROLE_ADMIN 등)
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken( userDetails, null, userDetails.getAuthorities() );

                // 요청 정보를 기반으로 인증 상세 정보 설정
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 인증 객체를 SecurityContext에 등록 → 이후 @AuthenticationPrincipal 등으로 접근 가능
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);    // 다음 필터 또는 컨트롤러로 요청 전달
    }
}
