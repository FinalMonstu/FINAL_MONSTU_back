package com.icetea.MonStu.shared.config;

import com.icetea.MonStu.member.enums.MemberRole;
import com.icetea.MonStu.shared.security.jwt.JwtAuthenticationFilter;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true) // @PreAuthorize 사용 설정
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, DaoAuthenticationProvider authenticationProvider) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)      // CSRF 보안 비활성화 (JWT 방식은 CSRF 보호 필요X)

                // CORS 설정 통합
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // 세션 미사용 (Stateless)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authenticationProvider(authenticationProvider)     // 사용자 인증 제공자 설정
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)     // UsernamePasswordAuthenticationFilter 전에 JWT 필터 등록

                // URL별 권한 관리 (화이트리스트 방식)
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()     // Preflight 요청(OPTIONS)은 무조건 허용(CORS 에러 방지)

                        // [Public - ALL]
                        .requestMatchers(
                                "/api/v2/auth/**",
                                "/api/v2/presets/**"
                        ).permitAll()

                        // [Public - GET]
                        .requestMatchers(HttpMethod.GET,
                                "/api/v2/posts/*",
                                "/api/v2/posts",
                                "/api/v2/posts/search"
                        ).permitAll()

                        // [Public - POST]
                        .requestMatchers(HttpMethod.POST,
                                "/api/v2/posts",
                                "/api/v2/translate"
                        ).permitAll()

//                        // [User]
//                        .requestMatchers(
//                                "/api/v2/histories",
//                                "/api/v2/members/**",
//                                "/api/v2/posts/**"
//                        ).authenticated()

                        // [Admin]
                        .requestMatchers(
                                "/api/v2/admin/**"
                        )
                        .hasRole(MemberRole.ADMIN.name())

                        .anyRequest().authenticated()   //나머지 모든 요청은 인증 필요
                )
                .build();
    }

    // CORS 설정 Bean
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();


        config.setAllowedOrigins(List.of(
                "http://localhost:3000",
                "https://localhost:3000"
        ));

        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));

        config.setAllowedHeaders(List.of("*")); //모든 헤더 허용

        // 노출할 헤더 (프론트에서 읽을 수 있게 허용)
        config.setExposedHeaders(List.of("Location", "Authorization"));

        // 자격 증명 허용 (쿠키 등)
        config.setAllowCredentials(true);

        // 6. Preflight 요청 캐시 시간 (1시간)
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
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

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}