package com.icetea.MonStu.controller;

import com.icetea.MonStu.dto.common.MemberDTO;
import com.icetea.MonStu.dto.request.MemberFilterRequest;
import com.icetea.MonStu.dto.request.SaveMemberRequest;
import com.icetea.MonStu.dto.request.UpdateMemberRequest;
import com.icetea.MonStu.dto.response.CustomPageableResponse;
import com.icetea.MonStu.dto.response.MessageResponse;
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
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mem")
@Tag(name = "Member API", description = "유저 관리")
public class MemberController {

    private final MemberService memberSVC;


    @Operation(summary = "신규 회원 정보 저장", description = "회원가입과는 달리 status,role 또한 지정된 데이터를 저장")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "저장 성공"),
            @ApiResponse(responseCode = "500", description = "오류 저장 실패")
    })
    @PostMapping("/save")
    public ResponseEntity<?> save(@Valid @RequestBody SaveMemberRequest request) {
        memberSVC.signup(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body( new MessageResponse("회원가입이 완료") );
    }


    @Operation(summary = "필터링된 회원 데이터 목록 반환", description = "페이지 정보와 필터링 정보를 이용, 필터링된 회원 데이터 목록 반환")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "반환 성공"),
            @ApiResponse(responseCode = "500", description = "반환 실패")
    })
    @PostMapping("/filter")
    public ResponseEntity<?> filterMembers(@RequestBody MemberFilterRequest filter, Pageable pageable) {
        System.out.println("filter:"+filter);
        System.out.println("pageable:"+pageable);
        Page<MemberDTO> page = memberSVC.filterMembers(filter,pageable);
        CustomPageableResponse<MemberDTO> result = CustomPageableResponse.mapper(page);
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
        MemberDTO result = memberSVC.getMember(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }


    @Operation(summary = "회원 정보 수정", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정 성공"),
            @ApiResponse(responseCode = "500", description = "수정 실패")
    })
    @PutMapping("/update")
    @PreAuthorize("hasRole(T(com.icetea.MonStu.enums.MemberRole).ADMIN.name())")
    public ResponseEntity<?> updateMember(@RequestBody UpdateMemberRequest request) {
        memberSVC.updateMember(request);
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
        memberSVC.deleteMember(id);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body( new MessageResponse("삭제 성공") );
    }


    @Operation(summary = "여러 회원 데이터 삭제", description = "전달받은 ID 목록을 이용, 해당 회원들 상태를 'DELETE'로 변경")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "삭제 성공"),
            @ApiResponse(responseCode = "500", description = "삭제_서버 오류 실패")
    })
    @PostMapping("/delete")
    @PreAuthorize("hasRole(T(com.icetea.MonStu.enums.MemberRole).ADMIN.name())")
    public ResponseEntity<?> deleteMembers(@RequestBody List<Long> ids) {
        System.out.println("idList:"+ids);
        memberSVC.signoutMembers(ids);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body( new MessageResponse("삭제 성공") );
    }


    @Operation(summary = "로그인 중인 회원의 정보 반환", description = "회원이 자기 정보 확인하는데 사용")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication authentication) {
        MemberDTO memberDTO = memberSVC.getMyInfo(authentication.getName());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body( memberDTO );
    }
}
