package com.icetea.MonStu.shared.common.enums;

import lombok.Getter;

@Getter
public enum Genre {
    WORD("WORD"),
    SENTENCE("SENTENCE");

    private final String code;

    Genre(String code) { this.code = code; }
}
