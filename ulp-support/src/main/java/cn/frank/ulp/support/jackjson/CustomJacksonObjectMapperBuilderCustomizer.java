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
package cn.frank.ulp.support.jackjson;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer;

import cn.frank.ulp.support.constant.UlpConstants;

import tools.jackson.databind.ext.javatime.deser.LocalDateDeserializer;
import tools.jackson.databind.ext.javatime.deser.LocalDateTimeDeserializer;
import tools.jackson.databind.ext.javatime.ser.LocalDateSerializer;
import tools.jackson.databind.ext.javatime.ser.LocalDateTimeSerializer;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.module.SimpleModule;

/**
 * 自定义 Jackson 对象映射构建器自定义器。
 * 用 {@link UlpConstants#DEFAULT_DATE_FORMATTER} / {@link UlpConstants#DEFAULT_DATE_TIME_FORMATTER}
 * 覆盖 Jackson 3 内置 java.time 默认 ISO-8601 序列化格式。
 */
public class CustomJacksonObjectMapperBuilderCustomizer implements JsonMapperBuilderCustomizer {

    @Override
    public void customize(JsonMapper.Builder builder) {
        SimpleModule dateTimeModule = new SimpleModule("UlpDateTimeModule")
            .addSerializer(LocalDate.class,
                new LocalDateSerializer(UlpConstants.DEFAULT_DATE_FORMATTER))
            .addSerializer(LocalDateTime.class,
                new LocalDateTimeSerializer(UlpConstants.DEFAULT_DATE_TIME_FORMATTER))
            .addDeserializer(LocalDate.class,
                new LocalDateDeserializer(UlpConstants.DEFAULT_DATE_FORMATTER))
            .addDeserializer(LocalDateTime.class,
                new LocalDateTimeDeserializer(UlpConstants.DEFAULT_DATE_TIME_FORMATTER));
        builder.addModule(dateTimeModule);
    }
}
