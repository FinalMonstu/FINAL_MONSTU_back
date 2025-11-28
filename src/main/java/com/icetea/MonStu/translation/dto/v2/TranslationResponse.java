package com.icetea.MonStu.translation.dto.v2;

import com.icetea.MonStu.shared.common.enums.Genre;
import com.icetea.MonStu.shared.common.enums.LanguageCode;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TranslationResponse {

    private Long id;

    private String originalText;  // 번역할 단어 또는 문장

    private String translatedText; // 번역된 단어 또는 문장

    private LanguageCode sourceLang;    // 원본 언어
    private LanguageCode targetLang;    // 번역 언어

    private Genre genre;    // "WORD" 또는 "SENTENCE"
}
