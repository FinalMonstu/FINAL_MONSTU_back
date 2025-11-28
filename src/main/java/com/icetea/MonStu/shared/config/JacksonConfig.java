package com.icetea.MonStu.shared.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class JacksonConfig {

    @Bean
    public SimpleModule enumModule() {
        SimpleModule module = new SimpleModule();
        // 모든 Enum 타입에 대해 커스텀 Deserializer 적용
        module.addDeserializer(Enum.class, new CaseInsensitiveEnumDeserializer());
        return module;
    }

    private static class CaseInsensitiveEnumDeserializer extends JsonDeserializer<Enum<?>> implements ContextualDeserializer {
        private Class<? extends Enum> enumClass;

        public CaseInsensitiveEnumDeserializer() {}

        public CaseInsensitiveEnumDeserializer(Class<? extends Enum> enumClass) {
            this.enumClass = enumClass;
        }

        @Override
        public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) {
            Class<?> rawClass = ctxt.getContextualType().getRawClass();
            return new CaseInsensitiveEnumDeserializer((Class<? extends Enum>) rawClass);
        }

        @Override
        public Enum<?> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String source = p.getText();
            if (source == null || source.isEmpty()) {
                return null;
            }

            String sourceTrimmed = source.trim();

            try {
                return Enum.valueOf(enumClass, sourceTrimmed.toUpperCase());
            } catch (IllegalArgumentException e) {
                for (Enum<?> enumConstant : enumClass.getEnumConstants()) {
                    String enumClean = enumConstant.name().replace("_", "");
                    String sourceClean = sourceTrimmed.replace("_", "").replace("-", "");

                    if (enumClean.equalsIgnoreCase(sourceClean)) {
                        return enumConstant;
                    }
                }
                throw ctxt.weirdStringException(source, enumClass, "Cannot convert value to Enum");
            }
        }
    }
}