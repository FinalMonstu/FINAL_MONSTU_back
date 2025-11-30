package com.icetea.MonStu.shared.common.api;

import com.icetea.MonStu.member.enums.MemberRole;
import com.icetea.MonStu.member.enums.MemberStatus;
import com.icetea.MonStu.shared.common.application.PresetService;
import com.icetea.MonStu.shared.security.annotation.RequireAdmin;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController("presetControllerV2")
@RequiredArgsConstructor
@RequestMapping("/api/v2/presets")
@Tag(name = "Lookup API", description = "고정(프리셋) 값 조회용 API")
public class PresetController {

    private final PresetService presetSvc;

    private final CacheControl cacheControl = CacheControl.maxAge(1, TimeUnit.HOURS);

    @Operation(summary = "국가언어 리스트 반환", description = " ex) 'Korean','English' ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "반환 성공"),
    })
    @GetMapping("/languages")
    public ResponseEntity<List<String>> getLanguageList(){
        List<String> response = presetSvc.getLanguageList();
        return ResponseEntity.ok()
                .cacheControl(cacheControl)
                .body(response);
    }


    @Operation(summary = "국가 리스트 반환", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "반환 성공"),
    })
    @GetMapping("/countries")
    public ResponseEntity<List<String>> getCountryList() {
        List<String> response = presetSvc.getCountryList();
        return ResponseEntity.ok()
                .cacheControl(cacheControl)
                .body(response);
    }


    @Operation(summary = "멤버 상태 요소 리스트 반환", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "반환 성공"),
    })
    @GetMapping("/member-statuses")
    @RequireAdmin
    public ResponseEntity<List<MemberStatus>> getMemberStatus() {
        List<MemberStatus> response = presetSvc.getMemberStatus();
        return ResponseEntity.ok()
                .body(response);
    }


    @Operation(summary = "멤버 역할 리스트 반환", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "반환 성공"),
    })
    @GetMapping("/member-roles")
    @RequireAdmin
    public ResponseEntity<List<MemberRole>> getMemberRole() {
        List<MemberRole> response = presetSvc.getMemberRole();
        return ResponseEntity.ok()
                .body(response);
    }
}
