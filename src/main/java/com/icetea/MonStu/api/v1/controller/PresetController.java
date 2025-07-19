package com.icetea.MonStu.api.v1.controller;

import com.icetea.MonStu.enums.MemberRole;
import com.icetea.MonStu.enums.MemberStatus;
import com.icetea.MonStu.service.PresetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Deprecated
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/preset")
@Tag(name = "Presetting API", description = "고정 요소 리스트 반환 API ex) 국가코드, 국가언어,언어코드")
public class PresetController {

    private final PresetService presetSvc;


    @Operation(summary = "국가언어 리스트 반환", description = " ex) 'Korean','English' ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "반환 성공"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping("/lang")
    public ResponseEntity<List<String>> getLanguageList(){
        List<String> result = presetSvc.getLanguageList();
        return result.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(result);
    }


    @Operation(summary = "국가 리스트 반환", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "반환 성공"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping("/coun")
    public ResponseEntity<List<String>> getCountryList() {
        List<String> result = presetSvc.getCountryList();
        return result.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(result);
    }


    @Operation(summary = "멤버 상태 배열 반환", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "반환 성공"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping("/mem/status")
    @PreAuthorize("hasRole(T(com.icetea.MonStu.enums.MemberRole).ADMIN.name())")
    public ResponseEntity<List<MemberStatus>> getMemberStatus() {
        List<MemberStatus> result = presetSvc.getMemberStatus();
        return result.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(result);
    }


    @Operation(summary = "멤버 역할 배열 반환", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "반환 성공"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping("/mem/role")
    @PreAuthorize("hasRole(T(com.icetea.MonStu.enums.MemberRole).ADMIN.name())")
    public ResponseEntity<List<MemberRole>> getMemberRole() {
        List<MemberRole> result = presetSvc.getMemberRole();
        return result.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(result);
    }
}
