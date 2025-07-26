package com.icetea.MonStu.api.v2.controller;

import com.icetea.MonStu.api.v2.dto.request.TranslationRequest;
import com.icetea.MonStu.api.v2.dto.response.TranslationResponse;
import com.icetea.MonStu.enums.TextUnit;
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

@RestController("translationControllerV2")
@RequiredArgsConstructor
@RequestMapping("/api/v2/translate")
@Tag(name = "Translation API", description = "단어,문장 번역")
public class TranslationController {

    private final TranslationService translationService;

    @Operation(summary = "단어, 문장 번역", description = "전달받은 단어,문장을 번역 후 반환")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "번역 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 문법"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @PostMapping
    public ResponseEntity<TranslationResponse> translationTarget(@Valid @RequestBody TranslationRequest translationRequest){
        TranslationResponse response = translationService.translate( translationRequest );
        return  response.getTranslatedText() != null
                ? new ResponseEntity<>(response, HttpStatus.OK)
                : new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);

        //For developing - Test
//        TranslationResponse t = TranslationResponse.builder()
//                .originalText("fun")
//                .translatedText("테스트")
//                .sourceLang("English")
//                .targetLang("Korean")
//                .textUnit(TextUnit.WORD)
//                .build();
//
//        return  t.getTranslatedText() != null
//                ? new ResponseEntity<>(t, HttpStatus.OK)
//                : new ResponseEntity<>(t, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
