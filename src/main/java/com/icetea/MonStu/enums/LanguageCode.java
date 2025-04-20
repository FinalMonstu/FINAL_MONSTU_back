package com.icetea.MonStu.enums;

import java.util.Arrays;
import java.util.List;

/*
    국가 언어 코드 (ISO 639-1)
*/
public enum LanguageCode {
    KO("Korean"),
    EN("English"),
    JA("Japanese"),
    ZH("Chinese"),
    FR("French"),
    ES("Spanish");

    private final String languageName;

    LanguageCode(String languageName) {
        this.languageName = languageName;
    }

    // 언어 이름을 반환 ex) LanguageCode.EN -> English
    public String getLanguageName() {
        return languageName;
    }

    // 코드(ES 등)를 반환 ex) LanguageCode.getCode("KO") -> Korean
    public static LanguageCode getCode(String code) {
        for (LanguageCode lang : values()) {
            if (lang.languageName.equalsIgnoreCase(code)) {
                return lang;
            }
        }
        throw new IllegalArgumentException("Invalid code: " + code);
    }

    // 모든 국가이름 출력  ex) ["Korean","English"..]
    public static List<String> getLanguageList() {
        return Arrays.stream(LanguageCode.values())
                .map(LanguageCode::getLanguageName)
                .toList();
    }

}
