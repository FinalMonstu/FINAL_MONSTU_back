package com.icetea.MonStu.member.api.v2;

import com.icetea.MonStu.member.application.MemberService;
import com.icetea.MonStu.member.dto.v2.request.AdminCreateMemberRequest;
import com.icetea.MonStu.member.dto.v2.request.FilterMemberRequest;
import com.icetea.MonStu.member.dto.v2.request.UpdateMemberRequest;
import com.icetea.MonStu.member.dto.v2.response.MemberSummaryResponse;
import com.icetea.MonStu.shared.dto.response.CustomPageableResponse;
import com.icetea.MonStu.shared.dto.response.MessageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("memberAdminControllerV2")
@RequiredArgsConstructor
@RequestMapping("/api/v2/admin/members")
@Tag(name = "Admin Member API", description = "관리자용 회원 관리 API")
public class MemberAdminController {

    private final MemberService memberSvc;


    @Operation(summary = "관리자용 : 신규 회원 정보 저장", description = "회원가입과는 달리 status,role 데이터까지 저장")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "저장 성공"),
            @ApiResponse(responseCode = "500", description = "저장 실패")
    })
    @PostMapping("")
    public ResponseEntity<MessageResponse> createMember(@Valid @RequestBody AdminCreateMemberRequest request) {
        memberSvc.createMember(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body( new MessageResponse("회원 추가 성공") );
    }


    @Operation(summary = "필터링된 회원 데이터 목록 반환", description = "페이지 정보와 필터링 정보를 이용 - 필터링된 회원 데이터 목록 반환")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "조회 성공"),
            @ApiResponse(responseCode = "500", description = "조회 실패")
    })
    @PostMapping("/search")
    public ResponseEntity<CustomPageableResponse<MemberSummaryResponse>> getMembersWithFilter(@RequestBody FilterMemberRequest filterMemberRequest, Pageable pageable ) {
        Page<MemberSummaryResponse> page = memberSvc.getPagedFilteredMembers(filterMemberRequest,pageable);
        CustomPageableResponse<MemberSummaryResponse> response = CustomPageableResponse.mapper(page);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }


    @Operation(summary = "회원 정보 상세 보기", description = "전달 받은 회원 ID를 이용, 회원 상세 데이터 반환")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "반환 성공"),
            @ApiResponse(responseCode = "500", description = "반환 실패")
    })
    @GetMapping("/{id}")
    public ResponseEntity<MemberSummaryResponse> getMember(@PathVariable Long id) {
        MemberSummaryResponse response = memberSvc.getMemberById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }


    @Operation(summary = "회원 정보 수정", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정 성공"),
            @ApiResponse(responseCode = "500", description = "수정 실패")
    })
    @PatchMapping("/{memberId}")
    public ResponseEntity<MessageResponse> updateMember(@PathVariable Long memberId,@RequestBody UpdateMemberRequest updateMemberRequest) {
        memberSvc.updateMember(memberId,updateMemberRequest);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body( new MessageResponse("수정 성공") );
    }


    @Operation(summary = "단일 회원 삭제", description = "회원 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "삭제 성공"),
            @ApiResponse(responseCode = "500", description = "삭제 실패")
    })
    @DeleteMapping("/{memberId}")
    public ResponseEntity<MessageResponse> deleteMember(@PathVariable Long memberId) {
        memberSvc.deleteMembers(List.of(memberId));
        return ResponseEntity
                .status(HttpStatus.OK)
                .body( new MessageResponse("삭제 성공") );
    }

    @Operation(summary = "다중 회원 데이터 삭제", description = "다중 회원 정보 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "삭제 성공"),
            @ApiResponse(responseCode = "500", description = "실패 실패")
    })
    @DeleteMapping("")
    public ResponseEntity<MessageResponse> deleteMembers(@RequestParam("ids") List<Long> ids) {
        memberSvc.deleteMembers(ids);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body( new MessageResponse("삭제 성공") );
    }
}
