package com.icetea.MonStu.api.v1.controller;

import com.icetea.MonStu.api.v1.dto.response.member.MemberResponse;
import com.icetea.MonStu.api.v1.dto.request.member.MemberFilterRequest;
import com.icetea.MonStu.api.v1.dto.request.member.SaveMemberRequest;
import com.icetea.MonStu.api.v1.dto.request.member.UpdateMemberRequest;
import com.icetea.MonStu.api.v1.dto.response.CustomPageableResponse;
import com.icetea.MonStu.api.v1.dto.response.MessageResponse;
import com.icetea.MonStu.enums.MemberStatus;
import com.icetea.MonStu.security.CustomUserDetails;
import com.icetea.MonStu.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mem")
@Tag(name = "Member API", description = "유저 관리")
public class MemberController {

    private final MemberService memberService;


    @Operation(summary = "신규 회원 정보 저장", description = "회원가입과는 달리 status,role 데이터까지 저장")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "저장 성공"),
            @ApiResponse(responseCode = "500", description = "오류 저장 실패")
    })
    @PostMapping("/save")
    public ResponseEntity<MessageResponse> save(@Valid @RequestBody SaveMemberRequest request) {
        memberService.register(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body( new MessageResponse("회원 추가 완료") );
    }


    @Operation(summary = "필터링된 회원 데이터 목록 반환", description = "페이지 정보와 필터링 정보를 이용 - 필터링된 회원 데이터 목록 반환")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "반환 성공"),
            @ApiResponse(responseCode = "500", description = "반환 실패")
    })
    @PostMapping("/filter")
    public ResponseEntity<CustomPageableResponse<MemberResponse>> getMembersWithFilter(@RequestBody MemberFilterRequest filter, Pageable pageable) {
        Page<MemberResponse> page = memberService.filterMembers(filter,pageable);
        CustomPageableResponse<MemberResponse> result = CustomPageableResponse.mapper(page);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }


    @Operation(summary = "회원 데이터 반환", description = "전달 받은 회원 ID를 이용, 회원 데이터 반환")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "반환 성공"),
            @ApiResponse(responseCode = "500", description = "반환 실패")
    })
    @GetMapping("/get")
    public ResponseEntity<?> getMember(@RequestParam Long id) {
        MemberResponse memberResponse = memberService.getById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(memberResponse);
    }


    @Operation(summary = "회원 정보 수정", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정 성공"),
            @ApiResponse(responseCode = "500", description = "수정 실패")
    })
    @PutMapping("/update")
    @PreAuthorize("hasRole(T(com.icetea.MonStu.enums.MemberRole).ADMIN.name())")
    public ResponseEntity<?> updateMember(@RequestBody UpdateMemberRequest request) {
        memberService.updateMember(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body( new MessageResponse("수정 성공") );
    }


    @Operation(summary = "회원 정보 삭제", description = "회원 status 속성을 'DELETE'로 변경")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "삭제 성공"),
            @ApiResponse(responseCode = "500", description = "삭제 실패")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole(T(com.icetea.MonStu.enums.MemberRole).ADMIN.name())")
    public ResponseEntity<?> deleteMember(@PathVariable Long id) {
        memberService.updateStatus(id, MemberStatus.DELETED);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body( new MessageResponse("삭제 성공") );
    }


    @Operation(summary = "여러 회원 데이터 삭제", description = "전달받은 ID 목록을 이용, 해당 회원들 상태를 'DELETE'로 변경")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "삭제 성공"),
            @ApiResponse(responseCode = "500", description = "삭제_서버 오류 실패")
    })
    @PostMapping("/delete/all")
    @PreAuthorize("hasRole(T(com.icetea.MonStu.enums.MemberRole).ADMIN.name())")
    public ResponseEntity<?> deleteAllMembers(@RequestBody List<Long> ids) {
        memberService.deactivateAll(ids);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body( new MessageResponse("삭제 성공") );
    }

    @Operation(summary = "탈퇴 멤버 재활성화", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "재활성화 성공"),
            @ApiResponse(responseCode = "500", description = "실패")
    })
    @PostMapping("/reactivate")
    public ResponseEntity<?> reactivateMember(@AuthenticationPrincipal CustomUserDetails userDetails) {
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
    public ResponseEntity<MemberResponse> me(@AuthenticationPrincipal CustomUserDetails userDetails) {
        System.out.println(userDetails.getId());
        MemberResponse memberResponse = memberService.getById(userDetails.getId());
        System.out.println(memberResponse);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(memberResponse);
    }
}
