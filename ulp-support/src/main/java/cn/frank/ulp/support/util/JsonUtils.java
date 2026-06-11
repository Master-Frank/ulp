/*
 * ulp-support - ULP support library
 * Copyright (c) 2022-Present Frank Zhang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.frank.ulp.support.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonInclude;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.cfg.DateTimeFeature;
import tools.jackson.databind.cfg.MapperConfig;
import tools.jackson.databind.introspect.Annotated;
import tools.jackson.databind.introspect.JacksonAnnotationIntrospector;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.ser.std.SimpleBeanPropertyFilter;
import tools.jackson.databind.ser.std.SimpleFilterProvider;

/**
 * Jackson 工具.
 */
public class JsonUtils {

    private static final JsonMapper MAPPER = JsonMapper.builder()
        .changeDefaultPropertyInclusion(v -> v.withValueInclusion(JsonInclude.Include.NON_NULL))
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .configure(DateTimeFeature.WRITE_DATES_AS_TIMESTAMPS, false).build();

    public JsonUtils() {
    }

    public static <T> T readValue(String content, Class<T> valueType) throws JsonUtilException {
        try {
            return hasText(content) ? MAPPER.readValue(content, valueType) : null;
        } catch (JacksonException e) {
            throw new JsonUtilException(e);
        }
    }

    public static <T> T readValue(String content, TypeReference<T> typeReference) {
        try {
            return hasText(content) ? MAPPER.readValue(content, typeReference) : null;
        } catch (JacksonException e) {
            throw new JsonUtilException(e);
        }
    }

    public static <T> T readValue(byte[] data, Class<T> valueType) throws JsonUtilException {
        try {
            return data != null && data.length > 0 ? MAPPER.readValue(data, valueType) : null;
        } catch (JacksonException e) {
            throw new JsonUtilException(e);
        }
    }

    public static <T> T readValue(byte[] data, TypeReference<T> typeReference) {
        try {
            return data != null && data.length > 0 ? MAPPER.readValue(data, typeReference) : null;
        } catch (JacksonException e) {
            throw new JsonUtilException(e);
        }
    }

    public static JsonNode readTree(String content) {
        try {
            return hasText(content) ? MAPPER.readTree(content) : null;
        } catch (JacksonException e) {
            throw new JsonUtilException(e);
        }
    }

    public static JsonNode readTree(JsonParser parser) {
        try {
            return MAPPER.readTree(parser);
        } catch (JacksonException e) {
            throw new JsonUtilException(e);
        }
    }

    public static String writeValueAsString(Object value) throws JsonUtilException {
        try {
            return MAPPER.writeValueAsString(value);
        } catch (JacksonException e) {
            throw new JsonUtilException(e);
        }
    }

    public static byte[] writeValueAsBytes(Object value) throws JsonUtilException {
        try {
            return MAPPER.writeValueAsBytes(value);
        } catch (JacksonException e) {
            throw new JsonUtilException(e);
        }
    }

    public static <T> T convertValue(Object fromValue,
                                     Class<T> toValueType) throws JsonUtilException {
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
            Map<String, Object> map = readValue(json, new TypeReference<Map<String, Object>>() {
            });
            for (String prop : properties) {
                if (prop.contains(".")) {
                    String[] parts = prop.split("\\.", 2);
                    if (map.containsKey(parts[0])) {
                        Object child = map.get(parts[0]);
                        map.put(parts[0],
                            readValue(
                                serializeExcludingProperties(child, new String[] { parts[1] }),
                                new TypeReference<Map<String, Object>>() {
                                }));
                    }
                } else {
                    map.remove(prop);
                }
            }
            return writeValueAsString(map);
        } catch (Exception e) {
            // fallback：使用 jackson FilterProvider
            try {
                JsonMapper mapper = MAPPER.rebuild()
                    .annotationIntrospector(new JacksonAnnotationIntrospector() {
                        @Override
                        public Object findFilterId(MapperConfig<?> config, Annotated a) {
                            return "__excludeFilter__";
                        }
                    }).build();
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
