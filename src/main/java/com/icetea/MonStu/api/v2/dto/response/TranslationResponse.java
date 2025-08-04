package com.icetea.MonStu.api.v2.dto.response;

import com.icetea.MonStu.enums.Genre;
import com.icetea.MonStu.enums.LanguageCode;
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

    private LanguageCode sourceLang;    // 원본 언어
    private LanguageCode targetLang;    // 번역 언어

    private Genre genre;    // "WORD" 또는 "SENTENCE"
}
