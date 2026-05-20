package cn.frank.ulp.support.util;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * Jackson 工具.
 */
public class JsonUtils {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        MAPPER.registerModule(new JavaTimeModule());
        MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    public JsonUtils() {
    }

    public static <T> T readValue(String content, Class<T> valueType) throws JsonUtilException {
        try {
            return hasText(content) ? MAPPER.readValue(content, valueType) : null;
        } catch (IOException e) {
            throw new JsonUtilException(e);
        }
    }

    public static <T> T readValue(String content, TypeReference<T> typeReference) {
        try {
            return hasText(content) ? MAPPER.readValue(content, typeReference) : null;
        } catch (IOException e) {
            throw new JsonUtilException(e);
        }
    }

    public static <T> T readValue(byte[] data, Class<T> valueType) throws JsonUtilException {
        try {
            return data != null && data.length > 0 ? MAPPER.readValue(data, valueType) : null;
        } catch (IOException e) {
            throw new JsonUtilException(e);
        }
    }

    public static <T> T readValue(byte[] data, TypeReference<T> typeReference) {
        try {
            return data != null && data.length > 0 ? MAPPER.readValue(data, typeReference) : null;
        } catch (IOException e) {
            throw new JsonUtilException(e);
        }
    }

    public static JsonNode readTree(String content) {
        try {
            return hasText(content) ? MAPPER.readTree(content) : null;
        } catch (IOException e) {
            throw new JsonUtilException(e);
        }
    }

    public static JsonNode readTree(JsonParser parser) {
        try {
            return MAPPER.readTree(parser);
        } catch (IOException e) {
            throw new JsonUtilException(e);
        }
    }

    public static String writeValueAsString(Object value) throws JsonUtilException {
        try {
            return MAPPER.writeValueAsString(value);
        } catch (IOException e) {
            throw new JsonUtilException(e);
        }
    }

    public static byte[] writeValueAsBytes(Object value) throws JsonUtilException {
        try {
            return MAPPER.writeValueAsBytes(value);
        } catch (IOException e) {
            throw new JsonUtilException(e);
        }
    }

    public static <T> T convertValue(Object fromValue, Class<T> toValueType) throws JsonUtilException {
        try {
            return fromValue == null ? null : MAPPER.convertValue(fromValue, toValueType);
        } catch (IllegalArgumentException e) {
            throw new JsonUtilException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> getNodeAsMap(JsonNode node) {
        if (node == null || node.isNull() || node.isMissingNode()) {
            return new HashMap<>();
        }
        return MAPPER.convertValue(node, Map.class);
    }

    public static String getNodeAsString(JsonNode node, String fieldName, String defaultValue) {
        JsonNode child;
        return node == null || (child = node.get(fieldName)) == null ? defaultValue
            : child.asText(defaultValue);
    }

    public static int getNodeAsInt(JsonNode node, String fieldName, int defaultValue) {
        JsonNode child;
        return node == null || (child = node.get(fieldName)) == null ? defaultValue
            : child.asInt(defaultValue);
    }

    public static boolean getNodeAsBoolean(JsonNode node, String fieldName, boolean defaultValue) {
        JsonNode child;
        return node == null || (child = node.get(fieldName)) == null ? defaultValue
            : child.asBoolean(defaultValue);
    }

    public static Date getNodeAsDate(JsonNode node, String fieldName) {
        JsonNode child;
        if (node == null || (child = node.get(fieldName)) == null) {
            return null;
        }
        long ts = child.asLong(-1L);
        return ts == -1L ? null : new Date(ts);
    }

    public static String serializeExcludingProperties(Object value, String... properties) {
        try {
            // ObjectMapper 上使用临时 mixin / filter 排除一级属性
            String json = writeValueAsString(value);
            @SuppressWarnings("unchecked")
            Map<String, Object> map = readValue(json, new TypeReference<Map<String, Object>>() {});
            for (String prop : properties) {
                if (prop.contains(".")) {
                    String[] parts = prop.split("\\.", 2);
                    if (map.containsKey(parts[0])) {
                        Object child = map.get(parts[0]);
                        map.put(parts[0], readValue(serializeExcludingProperties(child,
                            new String[] { parts[1] }), new TypeReference<Map<String, Object>>() {}));
                    }
                } else {
                    map.remove(prop);
                }
            }
            return writeValueAsString(map);
        } catch (Exception e) {
            // fallback：使用 jackson FilterProvider
            try {
                ObjectMapper mapper = MAPPER.copy();
                mapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector() {
                    @Override
                    public Object findFilterId(com.fasterxml.jackson.databind.introspect.Annotated a) {
                        return "__excludeFilter__";
                    }
                });
                SimpleFilterProvider provider = new SimpleFilterProvider().addFilter(
                    "__excludeFilter__", SimpleBeanPropertyFilter.serializeAllExcept(properties));
                return mapper.writer(provider).writeValueAsString(value);
            } catch (Exception ex) {
                throw new JsonUtilException(ex);
            }
        }
    }

    public static boolean hasText(CharSequence str) {
        if (!hasLength(str)) {
            return false;
        }
        int strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasLength(CharSequence str) {
        return str != null && !str.isEmpty();
    }

    /** 内部用于 {@link #serializeExcludingProperties} 的占位 mixin（保留以兼容潜在动态 mixin 场景）。 */
    @JsonFilter("__excludeFilter__")
    private static class ExcludeFilterMixin {
    }

    public static class JsonUtilException extends RuntimeException {
        public JsonUtilException(Throwable cause) {
            super(cause);
        }
    }
}
