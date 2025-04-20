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
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String jwt = null;
        String authHeader = request.getHeader("Authorization");
        System.out.println("Authorization header: " + authHeader);

        // 1) Authorization 헤더 우선
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
        } else {
            // 2) 헤더 없으면 쿠키에서 읽기
            if (request.getCookies() != null) {
                for (jakarta.servlet.http.Cookie cookie : request.getCookies()) {
                    if ("jwtToken".equals(cookie.getName())) {
                        jwt = cookie.getValue();
                        break;
                    }
                }
            } else {
                System.out.println("No cookies present");
            }
        }

        if (jwt != null) {
            // 1) 토큰이 존재하면, 토큰 안에서 사용자 이름(username)을 추출
            String username = jwtService.extractUsername(jwt);

            // 2) username이 null이 아니고, 아직 SecurityContext 에 인증 정보가 세팅되지 않았다면
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                // 3) UserDetailsService 로부터 해당 사용자의 상세 정보(UserDetails)를 로드
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // 4) 토큰이 그 사용자 정보와 매치되는지 검증
                if (jwtService.isTokenValid(jwt, userDetails)) {

                    // 5) 인증 완료된 사용자 정보를 바탕으로 Authentication 객체 생성
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,                  // principal: 사용자 상세 정보
                                    null,                         // credentials: (비밀번호 등) 이미 검증된 상태이므로 null
                                    userDetails.getAuthorities()  // authorities: 사용자의 권한 목록
                            );

                    // 6) 요청(request) 정보를 Authentication 객체에 설정
                    authToken.setDetails(new WebAuthenticationDetailsSource()
                            .buildDetails(request));

                    // 7) 최종적으로 SecurityContext 에 Authentication 객체를 저장 → “로그인된 사용자”로 인식됨
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    System.out.println("Authentication set for user: " + username);
                }
            }
        } else {
            // jwt가 헤더나 쿠키에도 없을 때
            System.out.println("No JWT token found in header or cookies");
        }

        // 8) 필터 체인의 다음 단계로 요청을 넘겨줌
        filterChain.doFilter(request, response);

    }
}
