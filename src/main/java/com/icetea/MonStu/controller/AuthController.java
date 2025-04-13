package com.icetea.MonStu.controller;

import com.icetea.MonStu.dto.request.FindEmailRequest;
import com.icetea.MonStu.dto.request.VerifiCodeRequest;
import com.icetea.MonStu.dto.request.EmailPasswordRequest;
import com.icetea.MonStu.dto.request.SignUpRequest;
import com.icetea.MonStu.dto.response.FindEmailResponse;
import com.icetea.MonStu.dto.response.JwtResponse;
import com.icetea.MonStu.dto.response.MessageResponse;
import com.icetea.MonStu.dto.response.VerifiCodeResponse;
import com.icetea.MonStu.exception.ConflictException;
import com.icetea.MonStu.security.JwtService;
import com.icetea.MonStu.service.AuthService;
import com.icetea.MonStu.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;   // 로그인 인증을 처리
    private final JwtService jwtService;    // JWT를 생성하고 검증
    private final UserDetailsService userDetailsService;
    private final MemberService memberService;
    private final AuthService authService;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody EmailPasswordRequest request, HttpServletResponse response) {
        authenticationManager.authenticate( new UsernamePasswordAuthenticationToken(request.email(), request.password()) );  //DB에서 가져온 비밀번호와 입력한 비밀번호 비교
        final UserDetails user = userDetailsService.loadUserByUsername(request.email()); //인증 성공 시, 사용자 정보를 가져옴
        final String jwtToken = jwtService.generateToken(user);
        jwtService.setOption(response,jwtToken);

        return ResponseEntity.ok(new MessageResponse("로그인 성공"));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignUpRequest request) {
        System.out.println("request: "+request.toString());
        memberService.signup(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("회원가입이 완료되었습니다.");
    }


    @Operation(summary = "이메일 인증 코드 전송", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "코드 생성 성공"),
            @ApiResponse(responseCode = "409", description = "이메일 전송 오류")
    })
    @PostMapping("/email/send")
    public ResponseEntity<?> sendEmailCode(@RequestBody VerifiCodeRequest request) {
        VerifiCodeResponse dto= authService.sendEmailCode(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }


    @Operation(summary = "이메일 중복 확인", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이메일 중복 없음"),
            @ApiResponse(responseCode = "409", description = "이메일 중복")
    })
    @GetMapping("/email/avail")
    public ResponseEntity<?> availEmail(@RequestParam String email) {
        boolean isExist = memberService.existsByEmail(email);
        if(isExist) throw new ConflictException("이미 존재하는 이메일입니다.");
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("사용할 수 있는 이메일입니다."));
    }


    @Operation(summary = "이메일 인증번호 검증", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "인증 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    @PostMapping("/email/verify")
    public ResponseEntity<?> verifyEmailCode(@RequestBody VerifiCodeRequest request) {
        authService.verifyEmailCode(request);
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("인증되었습니다."));
    }


    @Operation(summary = "비밀번호 재설정", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "재설정 완료"),
            @ApiResponse(responseCode = "500", description = "재설정 실패")
    })
    @PostMapping("/email/repass")
    public ResponseEntity<?> resetPassword(@RequestBody EmailPasswordRequest request) {
        memberService.resetPassword(request);
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("변경되었습니다."));
    }


    @Operation(summary = "이메일 찾기", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 완료"),
            @ApiResponse(responseCode = "500", description = "조회 실패")
    })
    @PostMapping("/email/find")
    public ResponseEntity<?> findEmail(@RequestBody FindEmailRequest request) {
        FindEmailResponse response = memberService.findEmail(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @Operation(summary = "회원 탈퇴", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "탈퇴 완료"),
            @ApiResponse(responseCode = "404", description = "이메일 인증 코드 오류"),
            @ApiResponse(responseCode = "500", description = "탈퇴 실패")
    })
    @PostMapping("/signout")
    public ResponseEntity<?> signOut(@RequestBody FindEmailRequest request) {
        FindEmailResponse response = memberService.findEmail(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
