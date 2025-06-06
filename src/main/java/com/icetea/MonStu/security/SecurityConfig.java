package com.icetea.MonStu.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;        // JWT 인증 필터 (요청마다 JWT 토큰 확인 및 인증 처리)


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, DaoAuthenticationProvider authenticationProvider) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)  // CSRF 보안 비활성화 (JWT 방식은 CSRF 보호 필요X)
                .cors(withDefaults())   // JWT
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))   // 세션을 사용X (JWT는 Stateless 방식)
                .securityContext(sc -> sc.requireExplicitSave(false))

                .authenticationProvider(authenticationProvider)   // 사용자 인증 제공자 설정
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)     // UsernamePasswordAuthenticationFilter 전에 JWT 필터 등록

                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(
                                "/api/post/save",
                                "/api/post/me/posts",

                                "/api/auth/signout",
                                "/api/auth/me",

                                "/api/mem/me",
                                "/api/mem/reactivate"
                        ).authenticated()

                        .requestMatchers( HttpMethod.DELETE,
                                "/api/post/{id}"
                        ).authenticated()

//                        .requestMatchers(HttpMethod.DELETE,
//                                "/api/mem/*",
//                                "/api/mem/list"
//                        ).hasRole(MemberRole.ADMIN.name())

                        .anyRequest().permitAll()   //나머지 모든 요청은 인증 불필요
                )
                .build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean   // 사용자의 아이디/비밀번호가 맞는지 확인해주는 역할
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean   // 비밀번호 인코더
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
