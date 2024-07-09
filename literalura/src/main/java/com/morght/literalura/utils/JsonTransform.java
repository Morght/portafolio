package com.morght.literalura.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonTransform implements IJsonTransform {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public <T> T toClass(String json, Class<T> clase) {
        try {
            return objectMapper.readValue(json, clase);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
