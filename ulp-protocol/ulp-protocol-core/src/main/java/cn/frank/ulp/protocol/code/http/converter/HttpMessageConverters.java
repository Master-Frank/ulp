/*
 * ulp-protocol-core - United Login Platform
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
package cn.frank.ulp.protocol.code.http.converter;

import org.springframework.http.converter.GenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.http.converter.json.JsonbHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.util.ClassUtils;

/**
 * Utility methods for {@link HttpMessageConverter}'s.
 *
 * @author Joe Grandja
 * @author luamas
 * @since 5.1
 */
public final class HttpMessageConverters {

    private static final boolean JACKSON2_JSON_PRESENT;
    private static final boolean JACKSON2_XML_PRESENT;

    private static final boolean GSON_PRESENT;

    private static final boolean JSONB_PRESENT;

    static {
        ClassLoader classLoader = HttpMessageConverters.class.getClassLoader();
        JACKSON2_JSON_PRESENT = ClassUtils.isPresent("tools.jackson.databind.ObjectMapper",
            classLoader) && ClassUtils.isPresent("tools.jackson.core.JsonGenerator", classLoader);

        // jackson-dataformat-xml is still on the jackson 2.x line under Boot 4 BOM (no tools.jackson.dataformat.xml yet).
        JACKSON2_XML_PRESENT = ClassUtils
            .isPresent("com.fasterxml.jackson.dataformat.xml.XmlMapper", classLoader);

        GSON_PRESENT = ClassUtils.isPresent("com.google.gson.Gson", classLoader);
        JSONB_PRESENT = ClassUtils.isPresent("jakarta.json.bind.Jsonb", classLoader);
    }

    private HttpMessageConverters() {
    }

    public static GenericHttpMessageConverter<Object> getJsonMessageConverter() {
        if (JACKSON2_JSON_PRESENT) {
            return new MappingJackson2HttpMessageConverter();
        }
        if (GSON_PRESENT) {
            return new GsonHttpMessageConverter();
        }
        if (JSONB_PRESENT) {
            return new JsonbHttpMessageConverter();
        }
        return null;
    }

    public static GenericHttpMessageConverter<Object> getXmlMessageConverter() {
        if (JACKSON2_XML_PRESENT) {
            return new MappingJackson2XmlHttpMessageConverter();
        }
        return null;
    }

}
