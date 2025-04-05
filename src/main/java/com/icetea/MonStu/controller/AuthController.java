package com.icetea.MonStu.controller;

import com.icetea.MonStu.dto.request.LoginRequest;
import com.icetea.MonStu.dto.request.SignUpRequest;
import com.icetea.MonStu.dto.response.JwtResponse;
import com.icetea.MonStu.repository.MemberRepository;
import com.icetea.MonStu.security.JwtService;
import com.icetea.MonStu.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;   // 로그인 인증을 처리
    private final JwtService jwtService;    // JWT를 생성하고 검증
    private final UserDetailsService userDetailsService;
    private final MemberService memberService;

    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService, UserDetailsService userDetailsService, MemberService memberService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.memberService = memberService;
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        authenticationManager.authenticate( new UsernamePasswordAuthenticationToken(request.email(), request.password()) );  //DB에서 가져온 비밀번호와 입력한 비밀번호 비교
        final UserDetails user = userDetailsService.loadUserByUsername(request.email()); //인증 성공 시, 사용자 정보를 가져옴
        final String jwt = jwtService.generateToken(user);
        return ResponseEntity.ok(new JwtResponse(jwt));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignUpRequest request) {
        memberService.signup(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("회원가입이 완료되었습니다.");
    }

}
