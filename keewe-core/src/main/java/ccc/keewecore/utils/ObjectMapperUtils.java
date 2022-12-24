package ccc.keewecore.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
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
            log.error(ex.getMessage());
            return "";
        }
    }

    public static <T> byte[] writeValueAsBytes(T src) {
        try {
            return objectMapper.writeValueAsBytes(src);
        } catch (JsonProcessingException ex) {
            log.error(ex.getMessage(), ex);
            return null;
        }
    }

    public static <T> T readValue(String src, Class<T> clazz) {
        try {
            return objectMapper.readValue(src, clazz);
        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);
            return null;
        }
    }

    public static <T> T readValue(byte[] src, Class<T> clazz) {
        try {
            return objectMapper.readValue(src, clazz);
        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);
            return null;
        }
    }
}
