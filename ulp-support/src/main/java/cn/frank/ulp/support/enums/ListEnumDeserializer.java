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
package cn.frank.ulp.support.enums;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.BeanProperty;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.ValueDeserializer;
import tools.jackson.databind.deser.std.StdDeserializer;

/**
 * 列表枚举反序列化器
 *
 * @author Frank Zhang
 * Created by support on 2020/8/18 21:35
 */
public class ListEnumDeserializer extends StdDeserializer<List<? extends BaseEnum>> {
    private static final Logger       log = LoggerFactory.getLogger(ListEnumDeserializer.class);

    private Class<? extends BaseEnum> clazz;

    public ListEnumDeserializer() {
        super((Class<?>) null);
    }

    public ListEnumDeserializer(Class<? extends BaseEnum> clazz) {
        super((Class<?>) null);
        this.clazz = clazz;
    }

    @Override
    public ValueDeserializer<?> createContextual(DeserializationContext context,
                                                BeanProperty property) {
        JavaType type = context.getContextualType();
        if (type != null) {
            JavaType contentType = type.getContentType();
            if (contentType != null) {
                Class<?> rawClass = contentType.getRawClass();
                if (BaseEnum.class.isAssignableFrom(rawClass)) {
                    @SuppressWarnings("unchecked")
                    Class<? extends BaseEnum> enumClass = (Class<? extends BaseEnum>) rawClass;
                    return new ListEnumDeserializer(enumClass);
                }
            }
        }
        return this;
    }

    @Override
    @SuppressWarnings("unused")
    public List<? extends BaseEnum> deserialize(JsonParser parser,
                                                DeserializationContext context) throws JacksonException {
        List<BaseEnum> list = new ArrayList<>();
        String text = parser.getText();
        if (StringUtils.hasText(text)) {
            String[] codes = text.split(",");
            for (String code : codes) {
                try {
                    BaseEnum[] enumConstants = clazz.getEnumConstants();
                    for (BaseEnum baseEnum : enumConstants) {
                        if (baseEnum.getCode().equals(code)) {
                            list.add(baseEnum);
                            break;
                        }
                    }
                } catch (Exception e) {
                    log.warn("反序列化枚举失败: {}", e.getMessage());
                }
            }
        }

        return list;
    }
}
