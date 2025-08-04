package com.icetea.MonStu.api.v2.controller;

import com.icetea.MonStu.api.v2.dto.request.TranslateTextHistoryRequest;
import com.icetea.MonStu.entity.History;
import com.icetea.MonStu.service.HistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("historyControllerV2")
@RequiredArgsConstructor
@RequestMapping("/api/v2/history")
@Tag(name = "History API", description = "단어/문장 기록 관리")
public class HistoryController {

    private final HistoryService historySvc;

    @Operation(summary = "번역 기록 저장", description = "전달받은 번역 기록 데이터베이스에 저장")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "저장 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @PostMapping("")
    public ResponseEntity<?> saveTranslateTextHistory(@Valid @RequestBody TranslateTextHistoryRequest translateTextHistoryRequest){
        historySvc.saveTranslateTextHistory(translateTextHistoryRequest);
        return ResponseEntity.ok().build();
    }

}
