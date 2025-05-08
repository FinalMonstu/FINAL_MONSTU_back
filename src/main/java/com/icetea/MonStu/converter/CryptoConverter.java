package com.icetea.MonStu.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.RequiredArgsConstructor;
import org.jasypt.encryption.StringEncryptor;

@Converter
@RequiredArgsConstructor
public class CryptoConverter implements AttributeConverter<String,String> {

    private final StringEncryptor jasypt;

    @Override
    public String convertToDatabaseColumn(String attribute) {
        return (attribute != null) ? jasypt.encrypt(attribute) : null;
    }

    // 엔티티에 복호화된 값 저장
    @Override
    public String convertToEntityAttribute(String dbData) {
        return (dbData != null) ? jasypt.decrypt(dbData) : null;
    }
}
