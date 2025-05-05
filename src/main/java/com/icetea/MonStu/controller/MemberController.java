package com.icetea.MonStu.controller;

import com.icetea.MonStu.dto.FilterRequest;
import com.icetea.MonStu.dto.common.MemberDTO;
import com.icetea.MonStu.dto.common.MemberLogDTO;
import com.icetea.MonStu.dto.request.AddmemberRequest;
import com.icetea.MonStu.dto.request.MemberFilterRequest;
import com.icetea.MonStu.dto.request.VerifiCodeRequest;
import com.icetea.MonStu.dto.response.CustomPageableResponse;
import com.icetea.MonStu.dto.response.MessageResponse;
import com.icetea.MonStu.dto.response.PostLiteResponse;
import com.icetea.MonStu.dto.response.VerifiCodeResponse;
import com.icetea.MonStu.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<?> save(@RequestBody AddmemberRequest request) {
        memberSVC.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입이 완료되었습니다.");
    }

    @Operation(summary = "회원 정보 삭제", description = "회원 status를 'DELETE'로 변경")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "삭제 성공"),
            @ApiResponse(responseCode = "500", description = "삭제 실패")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> del(@PathVariable Long id) {
        memberSVC.deleteMember(id);
        return ResponseEntity.status(HttpStatus.CREATED).body("삭제 되었습니다");
    }


    @Operation(summary = "회원 데이터 목록 반환", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "반환 성공"),
            @ApiResponse(responseCode = "500", description = "반환 실패")
    })
    @PostMapping("/find/filter")
    public ResponseEntity<?> findMembers(@RequestBody MemberFilterRequest filter, Pageable pageable) {
        System.out.println("filter:"+filter);
        System.out.println("pageable:"+pageable);
        Page<MemberDTO> page = memberSVC.findMembers(filter,pageable);
        CustomPageableResponse<MemberDTO> result = CustomPageableResponse.from(page);
        return  new ResponseEntity<>(result, HttpStatus.OK);
    }


    @Operation(summary = "회원 데이터 반환", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "반환 성공"),
            @ApiResponse(responseCode = "500", description = "반환 실패")
    })
    @GetMapping("/get")
    public ResponseEntity<?> findMembers(@RequestParam Long id) {
        System.out.println("id:"+id);
        MemberDTO result = memberSVC.getMember(id);
        return  new ResponseEntity<>(result,HttpStatus.OK);
    }


    @Operation(summary = "회원 데이터 삭제", description = "회원 상태를 'DELETE'로 변경")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "삭제 성공"),
            @ApiResponse(responseCode = "500", description = "삭제_서버 오류 실패")
    })
    @PostMapping("/del")
    public ResponseEntity<?> deleteMembers(@RequestBody List<Long> ids) {
        System.out.println("idList:"+ids);
        memberSVC.signoutMembers(ids);
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("삭제 성공"));
    }
}
