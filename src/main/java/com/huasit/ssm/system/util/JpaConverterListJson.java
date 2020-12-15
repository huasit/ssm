package com.huasit.ssm.system.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.huasit.ssm.business.question.entity.QuestionOption;

import javax.persistence.AttributeConverter;
import java.io.IOException;
import java.util.List;

public class JpaConverterListJson implements AttributeConverter<Object, String> {

    /**
     *
     */
    private final static ObjectMapper objectMapper = new ObjectMapper();

    /**
     *
     */
    @Override
    public String convertToDatabaseColumn(Object meta) {
        try {
            return objectMapper.writeValueAsString(meta);
        } catch (JsonProcessingException ex) {
            return null;
        }
    }

    /**
     *
     */
    @Override
    public Object convertToEntityAttribute(String s) {
        try {
            return objectMapper.readValue(s, new TypeReference<List<QuestionOption>>() {});
        } catch (IOException ex) {
            return null;
        }
    }
}
