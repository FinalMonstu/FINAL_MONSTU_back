package com.icetea.MonStu.api.v1.controller;

import com.icetea.MonStu.api.v1.dto.request.TransRequest;
import com.icetea.MonStu.api.v1.infra.TranslationClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ai")
@Tag(name = "Ai API", description = "AI API와 소통")
public class AiController {

    private final TranslationClient translationClient;


    @Operation(summary = "단어, 문장 번역", description = "전달받은 단어,문장을 번역 후 반환")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "번역 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @PostMapping("/trans")
    public ResponseEntity<TransRequest> transTarget(@Valid @RequestBody TransRequest transRequest){
        TransRequest result = translationClient.translate(transRequest);
        return  result.getTransed() != null
                ? new ResponseEntity<>(result, HttpStatus.OK)
                : new ResponseEntity<>(transRequest, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
