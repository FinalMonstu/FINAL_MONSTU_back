package com.icetea.MonStu.enums;

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

    public String getCountryName() {
        return countryName;
    }

    public String getAlpha3Code() {
        return alpha3Code;
    }

    public static CountryCode getAlpha3Code(String code) {
        for (CountryCode country : values()) {
            if (country.getAlpha3Code().equalsIgnoreCase(code)) {
                return country;
            }
        }
        throw new IllegalArgumentException("Invalid country code: " + code);

    }
}