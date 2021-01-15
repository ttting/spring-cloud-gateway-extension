package io.extension.springframework.cloud.gateway.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.cloud.gateway.route.RouteDefinition;

import java.io.IOException;
import java.util.List;

/**
 * Created by jiangtiteng on 2019-04-08
 */
public class JacksonUtils {
    private static JacksonUtils instance = new JacksonUtils();

    public static JacksonUtils getInstance() {
        return instance;
    }

    private ObjectMapper objectMapper;

    public JacksonUtils() {
        objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }


    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public String writeValueAsString(Object value) throws JsonProcessingException {
        return this.getObjectMapper().writeValueAsString(value);
    }

    public <T> T readValue(String content, TypeReference<List<RouteDefinition>> valueType) throws IOException {
        return this.objectMapper.readValue(content, valueType);
    }
}
