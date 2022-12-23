package ccc.keewecore.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ObjectMapperUtils {
    private static ObjectMapper objectMapper;

    @Autowired
    public ObjectMapperUtils(ObjectMapper objectMapper) {
        ObjectMapperUtils.objectMapper = objectMapper;
    }

    public static <T> String writeValueAsString(T src) {
        try {
            return objectMapper.writeValueAsString(src);
        } catch (JsonProcessingException ex) {
            return "";
        }
    }

    public static <T> T readValue(String src, Class<T> clazz) {
        try {
            return objectMapper.readValue(src, clazz);
        } catch (IOException e) {
            return null;
        }
    }

    public static <T> T readValue(byte[] src, Class<T> clazz) {
        try {
            return objectMapper.readValue(src, clazz);
        } catch (IOException e) {
            return null;
        }
    }
}
