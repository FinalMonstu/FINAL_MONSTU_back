package com.icetea.MonStu.api.v2.controller;

import com.icetea.MonStu.api.v2.dto.MessageResponse;
import com.icetea.MonStu.api.v2.dto.request.AdminCreateMemberRequest;
import com.icetea.MonStu.api.v2.dto.request.FilterMemberRequest;
import com.icetea.MonStu.api.v2.dto.request.UpdateMemberRequest;
import com.icetea.MonStu.api.v2.dto.request.UpdatePostRequest;
import com.icetea.MonStu.api.v2.dto.response.AdminMemberResponse;
import com.icetea.MonStu.api.v2.dto.response.CustomPageableResponse;
import com.icetea.MonStu.api.v2.dto.response.PostResponse;
import com.icetea.MonStu.enums.MemberStatus;
import com.icetea.MonStu.service.MemberService;
import com.icetea.MonStu.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("adminControllerV2")
@RequiredArgsConstructor
@RequestMapping("/api/v2/admin")
public class AdminController {

    private final MemberService memberService;
    private final PostService postService;

    /*----------Member----------------------------------------------------------------*/
    @Operation(summary = "관리자용 신규 회원 정보 저장", description = "회원가입과는 달리 status,role 데이터까지 저장")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "저장 성공"),
            @ApiResponse(responseCode = "500", description = "오류 저장 실패")
    })
    @PostMapping("/members")
    public ResponseEntity<MessageResponse> adminCreateMember(@Valid @RequestBody AdminCreateMemberRequest request) {
        memberService.createMember(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body( new MessageResponse("회원 추가 완료") );
    }


    @Operation(summary = "필터링된 회원 데이터 목록 반환", description = "페이지 정보와 필터링 정보를 이용 - 필터링된 회원 데이터 목록 반환")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "반환 성공"),
            @ApiResponse(responseCode = "500", description = "반환 실패")
    })
    @PostMapping("/members/search")
    public ResponseEntity<CustomPageableResponse<AdminMemberResponse>> getMembersWithFilter(@RequestBody FilterMemberRequest filterMemberRequest, Pageable pageable) {
        Page<AdminMemberResponse> page = memberService.filterMembers(filterMemberRequest,pageable);
        CustomPageableResponse<AdminMemberResponse> result = CustomPageableResponse.mapper(page);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }


    @Operation(summary = "회원 정보 상세 보기", description = "전달 받은 회원 ID를 이용, 회원 데이터 반환")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "반환 성공"),
            @ApiResponse(responseCode = "500", description = "반환 실패")
    })
    @GetMapping("/members/{id}")
    public ResponseEntity<AdminMemberResponse> getMember(@PathVariable Long id) {
        AdminMemberResponse memberResponse = memberService.getById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(memberResponse);
    }


    @Operation(summary = "회원 정보 수정", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정 성공"),
            @ApiResponse(responseCode = "500", description = "수정 실패")
    })
    @PatchMapping("/members/{id}")
    public ResponseEntity<MessageResponse> updateMember(@RequestBody UpdateMemberRequest updateMemberRequest) {
        memberService.updateMember(updateMemberRequest);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body( new MessageResponse("수정 성공") );
    }


    @Operation(summary = "회원 정보 삭제", description = "회원 status 속성을 'DELETE'로 변경")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "삭제 성공"),
            @ApiResponse(responseCode = "500", description = "삭제 실패")
    })
    @DeleteMapping("/members/{id}")
    public ResponseEntity<MessageResponse> deleteMember(@PathVariable Long id) {
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
    @DeleteMapping("/members")
    public ResponseEntity<MessageResponse> deleteAllMembers(@RequestParam("ids") List<Long> ids) {
        System.out.println("ids: "+ids);
        memberService.deactivateAll(ids);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body( new MessageResponse("삭제 성공") );
    }

    /*----------Post----------------------------------------------------------------*/
    @Operation(summary = "게시글과 로그 조회", description = "게시글 ID를 이용하여 게시물 & 로그 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "일치하는 게시물 없음"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping("/posts/{id}/logs")
    public ResponseEntity<PostResponse> findWithPostsAndLog(@PathVariable Long id ){
        PostResponse result = postService.findWithMemberAndLogById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body( result );
    }


    @Operation(summary = "여러 게시물 데이터 삭제", description = "전달받은 ID 목록을 이용, 해당 게시물 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "삭제 성공"),
            @ApiResponse(responseCode = "500", description = "서버 오류 실패")
    })
    @DeleteMapping("/posts")
    public ResponseEntity<MessageResponse> deletePosts(@RequestParam("ids") List<Long> ids) {
        postService.deletePosts(ids);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body( new MessageResponse("삭제 성공") );
    }


    @Operation(summary = "게시물 데이터 수정", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정 성공"),
            @ApiResponse(responseCode = "500", description = "수정_서버 오류 실패")
    })
    @PatchMapping("/posts/{id}")
    public ResponseEntity<MessageResponse> updatePost(@Valid @RequestBody UpdatePostRequest updatePostRequest) {
        postService.update(updatePostRequest);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body( new MessageResponse("수정 성공") );
    }
}
