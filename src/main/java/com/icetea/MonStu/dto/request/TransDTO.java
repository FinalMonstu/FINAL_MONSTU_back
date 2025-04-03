package com.icetea.MonStu.dto.request;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TransDTO {
    private String target;  // 번역할 단어 또는 문장
    private String transed; //번역된 단어 또는 문장

    private String oriLang; // 원본 언어
    private String transLang;   // 번역된 언어

    private String type;    // "WORD" 또는 "SENTENCE"
}
