package com.icetea.MonStu.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import static com.icetea.MonStu.validation.ValidationConstants.Translate_Target_REGEX;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TransDTO {

    /*  검증 실패 시 400 Bad Request
    *   null, ' ' 차단
    *   최소 한 글자 이상의 문자가 포함 */
    @NotBlank
    @Pattern( regexp = Translate_Target_REGEX )
    private String target;  // 번역할 단어 또는 문장

    private String transed; // 번역된 단어 또는 문장

    private String oriLang; // 원본 언어
    private String transLang;   // 번역 언어

    private String type;    // "WORD" 또는 "SENTENCE"
}
