package com.icetea.MonStu.controller;

import com.icetea.MonStu.dto.common.EmailDTO;
import com.icetea.MonStu.dto.common.MemberLiteInfoDTO;
import com.icetea.MonStu.dto.request.*;
import com.icetea.MonStu.dto.response.*;
import com.icetea.MonStu.security.JwtService;
import com.icetea.MonStu.service.AuthService;
import com.icetea.MonStu.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;   // 로그인 인증을 처리
    private final JwtService jwtService;                         // JWT를 생성하고 검증
    private final UserDetailsService userDetailsService;         //유저 정보

    private final MemberService memberService;
    private final AuthService authService;


    @Operation(summary = "로그인", description = "Email, Password 이용 - 로그인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "401", description = "정보 불일치"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        try{
            authenticationManager.authenticate( new UsernamePasswordAuthenticationToken(request.email(), request.password()) );  //DB에서 가져온 비밀번호와 입력한 비밀번호 비교

            UserDetails user = userDetailsService.loadUserByUsername(request.email());
            MemberLiteInfoDTO memberLiteInfoDTO = memberService.getMemberLiteInfoByEmail(request.email());  //사용자 정보 반환

            String jwtToken = jwtService.generateToken(user);
            jwtService.setOption(response,jwtToken);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(memberLiteInfoDTO);

        }catch (BadCredentialsException ex) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new MessageResponse("정보가 일치하지 않습니다"));
        }
    }


    @Operation(summary = "회원가입", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "가입 성공"),
            @ApiResponse(responseCode = "409", description = "이메일 중복 오류"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignUpRequest request) {
        System.out.println("회원가입: "+request);
        memberService.signup(request);
        return ResponseEntity .status(HttpStatus.CREATED).body("회원가입이 완료되었습니다.");
    }


    @Operation(summary = "이메일 인증 코드 전송", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "코드 생성 성공"),
            @ApiResponse(responseCode = "409", description = "이메일 전송 오류"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("/email/send")
    public ResponseEntity<?> sendEmailCode(@Valid @RequestBody SendEmailCodeRequest request) {
        VerifiCodeResponse result= authService.sendEmailCode(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }


    @Operation(summary = "이메일 인증번호 검증", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "인증 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    @PostMapping("/email/verify")
    public ResponseEntity<?> verifyEmailCode(@Valid @RequestBody VerifyEmailCodeRequest request) {
        boolean success = authService.verifyEmailCode(request);
        System.out.println("success: "+success);
        return success
                ? ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("인증되었습니다"))
                : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("인증 코드가 만료되었거나 일치하지 않습니다"));
    }


    @Operation(summary = "이메일 중복 검증", description = "데이터베이스에 해당 이메일이 존재 하는지 확인합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이메일 중복 없음"),
            @ApiResponse(responseCode = "409", description = "이메일 중복")
    })
    @GetMapping("/email/avail")
    public ResponseEntity<?> availEmail(@Valid @ModelAttribute EmailDTO dto) {
        memberService.existsByEmail(dto.email());
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("사용할 수 있는 이메일입니다."));
    }


    @Operation(summary = "비밀번호 재설정", description = "해당 이메일 정보의 비밀번호 재설정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "재설정 완료"),
            @ApiResponse(responseCode = "500", description = "재설정 실패")
    })
    @PostMapping("/repassword")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        memberService.resetPassword(request);
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("변경되었습니다"));
    }


    @Operation(summary = "이메일 찾기", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 완료"),
            @ApiResponse(responseCode = "500", description = "조회 실패")
    })
    @PostMapping("/email/find")
    public ResponseEntity<?> findEmail(@Valid @RequestBody FindEmailRequest request) {
        System.out.println("request: "+request.toString());
        EmailFindResponse result = memberService.findEmail(request);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }


    @Operation(summary = "회원 탈퇴", description = "회원의 상태속성을 'DELETE'로 변경")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "탈퇴 완료"),
            @ApiResponse(responseCode = "404", description = "이메일 인증 코드 오류"),
            @ApiResponse(responseCode = "500", description = "탈퇴 실패")
    })
    @PostMapping("/signout")
    public ResponseEntity<?> signOut(@AuthenticationPrincipal UserDetails ud) {
        memberService.signout();
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    @Operation(summary = "로그인 중인 회원의 정보 반환", description = "인증된 상태인지 확인하는데 사용")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "인증된 사용자"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) { return ResponseEntity.status(HttpStatus.NO_CONTENT).build();}
        System.out.println("authentication.getName(): "+authentication.getName());
        MemberLiteInfoDTO memberLiteInfoDTO = memberService.getMemberLiteInfoByEmail( authentication.getName() );
        return ResponseEntity.status(HttpStatus.OK).body(memberLiteInfoDTO);
    }

}
