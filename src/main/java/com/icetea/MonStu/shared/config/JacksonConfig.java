package com.icetea.MonStu.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.lang.reflect.Method;

@Configuration
public class JacksonConfig {

    @Bean
    public SimpleModule enumModule() {
        SimpleModule module = new SimpleModule();
        module.setDeserializerModifier(new BeanDeserializerModifier() {
            @Override
            public JsonDeserializer<?> modifyEnumDeserializer(DeserializationConfig config, JavaType type, BeanDescription beanDesc, JsonDeserializer<?> deserializer) {
                for (Method method : type.getRawClass().getDeclaredMethods()) {
                    if (method.isAnnotationPresent(JsonCreator.class)) {
                        return deserializer;
                    }
                }

                return new CaseInsensitiveEnumDeserializer((Class<? extends Enum>) type.getRawClass());
            }
        });
        return module;
    }

    private static class CaseInsensitiveEnumDeserializer extends JsonDeserializer<Enum<?>> implements ContextualDeserializer {
        private final Class<? extends Enum> enumClass;

        public CaseInsensitiveEnumDeserializer(Class<? extends Enum> enumClass) {
            this.enumClass = enumClass;
        }

        public CaseInsensitiveEnumDeserializer() {
            this.enumClass = null;
        }

        @Override
        public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) {
            return new CaseInsensitiveEnumDeserializer((Class<? extends Enum>) ctxt.getContextualType().getRawClass());
        }

        @Override
        public Enum<?> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String source = p.getText();
            if (source == null || source.isEmpty()) return null;

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