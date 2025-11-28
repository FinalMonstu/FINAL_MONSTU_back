package com.icetea.MonStu.shared.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

@Configuration
public class EnumMappingConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverterFactory(new CaseInsensitiveEnumConverterFactory());
    }

    private static class CaseInsensitiveEnumConverterFactory implements ConverterFactory<String, Enum> {
        @Override
        public <T extends Enum> Converter<String, T> getConverter(Class<T> targetType) {
            return new CaseInsensitiveEnumConverter<>(targetType);
        }
    }

    private static class CaseInsensitiveEnumConverter<T extends Enum<T>> implements Converter<String, T> {
        private final Class<T> enumType;

        public CaseInsensitiveEnumConverter(Class<T> enumType) {
            this.enumType = enumType;
        }

        @Override
        public T convert(String source) {
            if (source.isEmpty()) {
                return null;
            }

            String sourceTrimmed = source.trim();

            try {
                return Enum.valueOf(enumType, sourceTrimmed.toUpperCase());
            } catch (IllegalArgumentException e) {
                for (T enumConstant : enumType.getEnumConstants()) {
                    String enumNameClean = enumConstant.name().replace("_", "");
                    String sourceClean = sourceTrimmed.replace("_", "").replace("-", "");

                    if (enumNameClean.equalsIgnoreCase(sourceClean)) {
                        return enumConstant;
                    }
                }
                throw e;
            }
        }
    }
}