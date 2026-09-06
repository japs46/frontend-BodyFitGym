package com.japs.frontend.bodyfitgym.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public final class ObjectMapperProvider {

    private static final ObjectMapper MAPPER;

    static {
        MAPPER = new ObjectMapper();
        MAPPER.registerModule(new JavaTimeModule());
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // Fechas como ISO-8601
    }

    private ObjectMapperProvider() {
        // Constructor privado para evitar instanciación
    }

    public static ObjectMapper get() {
        return MAPPER;
    }

    public static String toJson(Object obj) throws JsonProcessingException {
        return MAPPER.writeValueAsString(obj);
    }

    public static <T> T readValue(String json, Class<T> clazz) throws JsonProcessingException {
        return MAPPER.readValue(json, clazz);
    }

    public static <T> T readValue(String json, TypeReference<T> typeRef) throws JsonProcessingException {
        return MAPPER.readValue(json, typeRef);
    }

    public static <T> T convertValue(Object fromValue, Class<T> toValueType) {
        return MAPPER.convertValue(fromValue, toValueType);
    }

    public static <T> T convertValue(Object data, TypeReference<T> typeReference) {
        return MAPPER.convertValue(data, typeReference);
    }
    
}
