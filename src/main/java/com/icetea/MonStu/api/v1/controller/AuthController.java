package com.icetea.MonStu.api.v1.controller;

import com.icetea.MonStu.api.v1.dto.request.auth.*;
import com.icetea.MonStu.api.v1.dto.response.auth.EmailFindResponse;
import com.icetea.MonStu.api.v1.dto.response.auth.LiteMemberResponse;
import com.icetea.MonStu.api.v1.dto.response.*;
import com.icetea.MonStu.api.v1.dto.response.auth.VerifiCodeResponse;
import com.icetea.MonStu.enums.MemberStatus;
import com.icetea.MonStu.security.CustomUserDetails;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Deprecated
@RestController
@RequiredArgsConstructor
//@RequestMapping("/api/auth")
public class AuthController {

    private final MemberService memberService;
    private final AuthService authService;


//    @Operation(summary = "로그인", description = "Email & Password 이용 - 로그인, 사용자 정보 반환")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "로그인 성공"),
//            @ApiResponse(responseCode = "401", description = "정보 불일치"),
//            @ApiResponse(responseCode = "500", description = "서버 오류")
//    })
//    @PostMapping("/login")
//    public ResponseEntity<LiteMemberResponse> login(@RequestBody LoginRequest request, HttpServletResponse response) {
//        LiteMemberResponse member = authService.login(request, response);
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .body(member);
//    }


//    @Operation(summary = "회원가입", description = "")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "201", description = "가입 성공"),
//            @ApiResponse(responseCode = "409", description = "이메일 중복 오류"),
//            @ApiResponse(responseCode = "500", description = "서버 오류")
//    })
//    @PostMapping("/signup")
//    public ResponseEntity<MessageResponse> signup(@Valid @RequestBody SignUpRequest request) {
//        memberService.register(request);
//        return ResponseEntity
//                .status(HttpStatus.CREATED)
//                .body(new MessageResponse("회원가입이 완료되었습니다."));
//    }


//    @Operation(summary = "이메일 인증 코드 전송", description = "")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "201", description = "코드 생성 성공"),
//            @ApiResponse(responseCode = "409", description = "이메일 전송 오류"),
//            @ApiResponse(responseCode = "500", description = "서버 오류")
//    })
//    @PostMapping("/email/send")
//    public ResponseEntity<VerifiCodeResponse> sendEmailCode(@Valid @RequestBody SendEmailCodeRequest request) {
//        VerifiCodeResponse savedCode= authService.sendEmailCode(request);
//        return ResponseEntity
//                .status(HttpStatus.CREATED)
//                .body(savedCode);
//    }


//    @Operation(summary = "이메일 인증번호 검증", description = "")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "인증 성공"),
//            @ApiResponse(responseCode = "401", description = "인증 실패")
//    })
//    @PostMapping("/email/verify")
//    public ResponseEntity<MessageResponse> verifyEmailCode(@Valid @RequestBody VerifyEmailCodeRequest request) {
//        boolean success = authService.verifyEmailCode(request);
//        return success
//                ? ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("인증되었습니다"))
//                : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("인증 코드가 만료되었거나 일치하지 않습니다"));
//    }


//    @Operation(summary = "사용 가능 이메일 검증", description = "해당 이메일이 이미 가입된 이메일인지 확인")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "이메일 사용 가능"),
//            @ApiResponse(responseCode = "409", description = "이메일 중복")
//    })
//    @GetMapping("/email/avail")
//    public ResponseEntity<MessageResponse> availEmail(@Valid @ModelAttribute EmailRequest request) {
//        memberService.existsByEmail(request.email());
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .body(new MessageResponse("사용할 수 있는 이메일입니다."));
//    }


//    @Operation(summary = "비밀번호 재설정", description = "해당 이메일 정보의 비밀번호 재설정")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "재설정 완료"),
//            @ApiResponse(responseCode = "500", description = "재설정 실패")
//    })
//    @PostMapping("/repassword")
//    public ResponseEntity<MessageResponse> resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {
//        memberService.resetPassword(resetPasswordRequest);
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .body(new MessageResponse("비밀번호가 변경되었습니다."));
//    }


//    @Operation(summary = "이메일 찾기", description = "핸드폰 번호 & 닉네임을 이용 - 이메일 조회")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "조회 완료"),
//            @ApiResponse(responseCode = "500", description = "조회 실패")
//    })
//    @PostMapping("/email/find")
//    public ResponseEntity<EmailFindResponse> findEmail(@Valid @RequestBody FindEmailRequest request) {
//        EmailFindResponse email = memberService.findEmail(request);
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .body(email);
//    }


//    @Operation(summary = "회원 탈퇴", description = "회원의 상태속성을 'DELETE'로 변경")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "탈퇴 완료"),
//            @ApiResponse(responseCode = "404", description = "이메일 인증 코드 오류"),
//            @ApiResponse(responseCode = "500", description = "탈퇴 실패")
//    })
//    @PostMapping("/signout")
//    public ResponseEntity<Void> signOut(@AuthenticationPrincipal CustomUserDetails userDetails) {
//        memberService.updateStatus(userDetails.getId(), MemberStatus.DELETED);
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .build();
//    }


//    @Operation(summary = "로그인 중인 회원의 정보 반환", description = "")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "인증된 사용자"),
//            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
//            @ApiResponse(responseCode = "500", description = "서버 오류")
//    })
//    @GetMapping("/me")
//    public ResponseEntity<LiteMemberResponse> me(@AuthenticationPrincipal CustomUserDetails userDetails) {
//        LiteMemberResponse member = memberService.getLiteById( userDetails.getId() );
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .body(member);
//    }

}
