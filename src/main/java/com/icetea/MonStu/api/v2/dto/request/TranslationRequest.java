package com.icetea.MonStu.api.v2.dto.request;


import com.icetea.MonStu.enums.Genre;
import com.icetea.MonStu.enums.LanguageCode;
import com.icetea.MonStu.enums.TextUnit;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import static com.icetea.MonStu.validation.ValidationConstants.Translate_Target_REGEX;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class TranslationRequest {

    /*  검증 실패 시 400 Bad Request
    *   null, ' ' 차단
    *   최소 한 글자 이상의 문자가 포함 */
    @NotBlank
    @Size(max = 115, message = "{TransRequest.target.Size}")
    @Pattern( regexp = Translate_Target_REGEX )
    private String originalText;  // 번역할 단어 또는 문장

    private LanguageCode sourceLang;    // 원본 언어
    private LanguageCode targetLang;    // 번역 언어

    private Genre genre;    // "WORD" 또는 "SENTENCE"
}
