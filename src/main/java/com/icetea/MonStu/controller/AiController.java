package com.icetea.MonStu.controller;

import com.icetea.MonStu.dto.request.TransDTO;
import com.icetea.MonStu.service.AiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/ai")
@Tag(name = "Ai API", description = "AI 사용 API")
public class AiController {

    private AiService aiSvc;

    public AiController(AiService aiSvc) {
        this.aiSvc = aiSvc;
    }

    @Operation(summary = "단어, 문장 번역", description = "전달받은 단어,문장을 다른 언어의 타입으로 번역, 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "번역 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @PostMapping("/trans")
    public ResponseEntity<String> transTarget(@RequestBody TransDTO transDTO){
        if(transDTO.getTarget() == null) return ResponseEntity.badRequest().body("Invalid request: TransDTO cannot be null");
        log.info("transDTO : {}", transDTO.toString());

        String result = aiSvc.transTarget(transDTO);
        return  result != null ?
                new ResponseEntity<>(result, HttpStatus.OK) :
                new ResponseEntity<>("", HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
