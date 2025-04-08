package com.icetea.MonStu.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;
import java.util.List;

public enum CountryCode {
    AUS("Australia", "AUS"),
    AUT("Austria", "AUT"),
    BRA("Brazil", "BRA"),
    CAN("Canada", "CAN"),
    CHN("China", "CHN"),
    FRA("France", "FRA"),
    DEU("Germany", "DEU"),
    IND("India", "IND"),
    ITA("Italy", "ITA"),
    JPN("Japan", "JPN"),
    KOR("South Korea", "KOR"),
    RUS("Russia", "RUS"),
    ESP("Spain", "ESP"),
    SWE("Sweden", "SWE"),
    GBR("United Kingdom", "GBR"),
    USA("United States", "USA");

    private final String countryName;
    private final String alpha3Code;

    CountryCode(String countryName, String alpha3Code) {
        this.countryName = countryName;
        this.alpha3Code = alpha3Code;
    }

    public String getAlpha3Code() {
        return alpha3Code;
    }

    @JsonValue  //Java 객체를 → JSON으로 변환할 때 어떤 값을 사용할지를 지정.
    public String getCountryName() {
        return countryName;
    }

    @JsonCreator    //문자열을 enum이나 객체로 바꿔줄 때, JSON 값을 → Java 객체로 변환할 때 어떤 생성자나 메서드를 사용할지를 지정.
    public static CountryCode fromCountryName(String name) {
        for (CountryCode country : values()) {
            if (country.getCountryName().equalsIgnoreCase(name)) {
                return country;
            }
        }
        throw new IllegalArgumentException("Invalid country name: " + name);
    }

    public static List<String> getCountryNames(){
        return Arrays.stream(CountryCode.values())
                .map(CountryCode::getCountryName)
                .toList();
    }
}