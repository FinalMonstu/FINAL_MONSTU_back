package com.icetea.MonStu.api.v2.controller;

import com.icetea.MonStu.api.v2.dto.request.TranslationRequest;
import com.icetea.MonStu.api.v2.dto.response.TranslationResponse;
import com.icetea.MonStu.service.TranslationService;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/translate")
@Tag(name = "Translation API", description = "단어,문장 번역")
public class TranslationController {

    private final TranslationService translationService;

    @Operation(summary = "단어, 문장 번역", description = "전달받은 단어,문장을 번역 후 반환")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "번역 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @PostMapping
    public ResponseEntity<TranslationResponse> transTarget(@Valid @RequestBody TranslationRequest req){
        System.out.println(req.toString());
        TranslationResponse response = translationService.translate(req);
        return  response.getTranslatedText() != null
                ? new ResponseEntity<>(response, HttpStatus.OK)
                : new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
