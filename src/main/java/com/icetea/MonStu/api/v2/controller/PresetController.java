package com.icetea.MonStu.api.v2.controller;

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

@Slf4j
@RestController("presetControllerV2")
@RequiredArgsConstructor
@RequestMapping("/api/v2/presets")
@Tag(name = "Lookup API", description = "고정(프리셋) 값 조회용 API")
public class PresetController {

    private final PresetService presetSvc;


    @Operation(summary = "국가언어 리스트 반환", description = " ex) 'Korean','English' ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "반환 성공"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping("/languages")
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
    @GetMapping("/countries")
    public ResponseEntity<List<String>> getCountryList() {
        List<String> result = presetSvc.getCountryList();
        return result.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(result);
    }


    @Operation(summary = "멤버 상태 요소 리스트 반환", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "반환 성공"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping("/member-statuses")
    @PreAuthorize("hasRole(T(com.icetea.MonStu.enums.MemberRole).ADMIN.name())")
    public ResponseEntity<List<MemberStatus>> getMemberStatus() {
        List<MemberStatus> result = presetSvc.getMemberStatus();
        return result.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(result);
    }


    @Operation(summary = "멤버 역할 리스트 반환", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "반환 성공"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping("/member-roles")
    @PreAuthorize("hasRole(T(com.icetea.MonStu.enums.MemberRole).ADMIN.name())")
    public ResponseEntity<List<MemberRole>> getMemberRole() {
        List<MemberRole> result = presetSvc.getMemberRole();
        return result.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(result);
    }
}
