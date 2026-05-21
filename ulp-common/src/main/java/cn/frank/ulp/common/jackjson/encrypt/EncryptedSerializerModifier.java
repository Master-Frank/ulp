/*
 * ulp-common - United Login Platform
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
package cn.frank.ulp.common.jackjson.encrypt;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;

/**
 * @author Frank Zhang
 */
public class EncryptedSerializerModifier extends BeanSerializerModifier {

    private final JsonEncryptType jsonEncryptType;

    public EncryptedSerializerModifier() {
        this.jsonEncryptType = null;
    }

    public EncryptedSerializerModifier(JsonEncryptType jsonEncryptType) {
        this.jsonEncryptType = jsonEncryptType;
    }

    @Override
    public List<BeanPropertyWriter> changeProperties(SerializationConfig config,
                                                     BeanDescription beanDesc,
                                                     List<BeanPropertyWriter> beanProperties) {
        /*
            遍历beanProperties处理Encrypt.class注解
         */
        List<BeanPropertyWriter> newWriter = new ArrayList<>();
        for (BeanPropertyWriter writer : beanProperties) {
            JsonPropertyEncrypt annotation = writer.getAnnotation(JsonPropertyEncrypt.class);
            if (null == annotation) {
                newWriter.add(writer);
            } else {
                JsonEncryptType deserializer = jsonEncryptType;
                if (jsonEncryptType == null) {
                    deserializer = annotation.deserializer();
                }
                JsonSerializer<Object> serializer = new EncryptedJsonSerializer(
                    writer.getSerializer(), deserializer);
                writer.assignSerializer(serializer);
                newWriter.add(writer);
            }
        }

        return newWriter;
    }
}
