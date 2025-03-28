package com.icetea.MonStu.enums;

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

    // 언어 이름을 반환
    public String getLanguageName() {
        return languageName;
    }

    // 코드(ES 등)를 반환
    public static LanguageCode getCode(String code) {
        for (LanguageCode lang : values()) {
            if (lang.name().equals(code)) {
                return lang;
            }
        }
        throw new IllegalArgumentException("Invalid code: " + code);
    }
}
