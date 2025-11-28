package com.icetea.MonStu.shared.util;

public interface RegexPatterns {

    // !@#$%*? 중 하나 포함
    String PASSWORD_REGEX = ".*[!@#$%*?].*";

    String PHONE_REGEX = "\\d{2,3}-\\d{3,4}-\\d{4}";

    // 최소 한 글자 이상의 문자가 포함
    String Translate_Target_REGEX = ".*[\\p{L}].*";

}
