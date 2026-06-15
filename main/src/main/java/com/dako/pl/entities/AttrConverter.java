package com.dako.pl.entities;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.lang.reflect.Type;
import java.util.Map;

@Converter
public class AttrConverter implements AttributeConverter<Map<String, Object>, String> {
    private final static Gson GSON = new Gson();

    @Override
    public String convertToDatabaseColumn(Map<String, Object> attribute) {
        return GSON.toJson(attribute);
    }

    @Override
    public Map<String, Object> convertToEntityAttribute(String dbData) {
        Type mapType = new TypeToken<Map<String, Object>>() {
        }.getType();
        return GSON.fromJson(dbData, mapType);
    }
}