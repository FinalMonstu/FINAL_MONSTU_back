package com.icetea.MonStu.api.v2.controller;

import com.icetea.MonStu.api.v2.dto.MessageResponse;
import com.icetea.MonStu.api.v2.dto.response.MemberProfileResponse;
import com.icetea.MonStu.enums.MemberStatus;
import com.icetea.MonStu.security.CustomUserDetails;
import com.icetea.MonStu.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/members")
@Tag(name = "Member API", description = "유저 관리")
public class MemberController {

    private final MemberService memberService;


    @Operation(summary = "탈퇴 멤버 재활성화", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "재활성화 성공"),
            @ApiResponse(responseCode = "500", description = "실패")
    })
    @PostMapping("/reactivate")
    public ResponseEntity<MessageResponse> reactivateMember(@AuthenticationPrincipal CustomUserDetails userDetails) {
        memberService.updateStatus(userDetails.getId(), MemberStatus.ACTIVE);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body( new MessageResponse("재활성화 성공") );
    }

    @Operation(summary = "로그인 중인 회원의 정보 반환", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/me")
    public ResponseEntity<MemberProfileResponse> me(@AuthenticationPrincipal CustomUserDetails userDetails) {
        MemberProfileResponse memberResponse = memberService.getMemberSummaryById(userDetails.getId());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(memberResponse);
    }
}
