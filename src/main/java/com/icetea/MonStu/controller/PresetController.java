package com.icetea.MonStu.controller;

import com.icetea.MonStu.enums.MemberRole;
import com.icetea.MonStu.enums.MemberStatus;
import com.icetea.MonStu.service.PresetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/preset")
@Tag(name = "Presetting API", description = "고정 요소 리스트 반환 API ex) 국가코드, 국가언어,언어코드")
public class PresetController {

    private final PresetService presetSvc;

    public PresetController(PresetService presetSvc) {
        this.presetSvc = presetSvc;
    }


    @Operation(summary = "국가언어 리스트 반환", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "반환 성공"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping("/lang")
    public ResponseEntity<List<String>> getLanguageList(){
        List<String> result = presetSvc.getLanguageList();
        if(result.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(result);
    }


    @Operation(summary = "국가 리스트 반환", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "반환 성공"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping("/coun")
    public ResponseEntity<List<String>> getCountryList() {
        List<String> result = presetSvc.getCountryList();
        if(result.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(result);
    }


    @Operation(summary = "멤버 상태 배열 반환", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "반환 성공"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping("/mem/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<MemberStatus>> getMemberStatus() {
        List<MemberStatus> result = presetSvc.getMemberStatus();
        if(result.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(result);
    }


    @Operation(summary = "멤버 역할 배열 반환", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "반환 성공"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping("/mem/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<MemberRole>> getMemberRole() {
        List<MemberRole> result = presetSvc.getMemberRole();
        if(result.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(result);
    }

}
