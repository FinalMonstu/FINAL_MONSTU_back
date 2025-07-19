package com.icetea.MonStu.api.v2.dto.response;

import com.icetea.MonStu.enums.TextUnit;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TranslationResponse {

    private String originalText;  // 번역할 단어 또는 문장

    private String translatedText; // 번역된 단어 또는 문장

    private String sourceLang; // 원본 언어
    private String targetLang;   // 번역 언어

    private TextUnit textUnit;    // "WORD" 또는 "SENTENCE"
}
