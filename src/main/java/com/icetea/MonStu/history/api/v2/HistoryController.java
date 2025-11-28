package com.icetea.MonStu.history.api.v2;

import com.icetea.MonStu.post.dto.v2.request.PostHistoryLinkRequest;
import com.icetea.MonStu.history.application.HistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("historyControllerV2")
@RequiredArgsConstructor
@RequestMapping("/api/v2/histories")
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
    public ResponseEntity<?> linkPostWithHistories(@Valid @RequestBody PostHistoryLinkRequest postHistoryLinkRequest){
        historySvc.linkPostWithHistories(postHistoryLinkRequest);
        return ResponseEntity.ok().build();
    }

}
