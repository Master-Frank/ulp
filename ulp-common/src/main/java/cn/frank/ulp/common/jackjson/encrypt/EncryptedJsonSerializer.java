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

import java.io.StringWriter;

import org.apache.commons.lang3.StringUtils;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueSerializer;

/**
 * @author Frank Zhang
 */
public class EncryptedJsonSerializer extends ValueSerializer<Object> {

    /**
     * 默认序列化工具对象
     */
    private final ValueSerializer<Object> serializer;
    private final JsonEncryptType        serializerJsonEncryptType;

    public EncryptedJsonSerializer() {
        this.serializer = null;
        this.serializerJsonEncryptType = null;
    }

    public EncryptedJsonSerializer(ValueSerializer<Object> serializer,
                                   JsonEncryptType jsonEncryptType) {
        this.serializer = serializer;
        this.serializerJsonEncryptType = jsonEncryptType;
    }

    @Override
    public void serialize(Object obj, JsonGenerator jsonGenerator,
                          SerializationContext ctxt) throws JacksonException {
        //空对象或空字符串不处理。
        if (obj == null || StringUtils.isEmpty(String.valueOf(obj))) {
            if (serializer == null) {
                ctxt.writeValue(jsonGenerator, obj);
            } else {
                serializer.serialize(obj, jsonGenerator, ctxt);
            }
            return;
        }
        /*
            生成一个新的JsonGenerator，用于将obj写入。
            Jackson 3: 通过 jsonGenerator.objectWriteContext() 拿到 ObjectWriteContext，
            再 createGenerator(Writer) 派生一个同配置的嵌套 generator。
         */
        StringWriter stringWriter = new StringWriter();
        JsonGenerator nestedGenerator = jsonGenerator.objectWriteContext()
            .createGenerator(stringWriter);

        /*
            将数据写入到新生成的JsonGenerator中
         */
        if (serializer == null) {
            ctxt.writeValue(nestedGenerator, obj);
        } else {
            serializer.serialize(obj, nestedGenerator, ctxt);
        }

        nestedGenerator.close();
        /*
            JsonGenerator会生成一个带双引号的字符串， 将数据加密后写入。
         */
        String value = stringWriter.getBuffer().toString();
        try {
            String newValue = value.substring(1, value.length() - 1);
            if (StringUtils.isNotEmpty(newValue)) {
                if (JsonEncryptType.ENCRYPT == serializerJsonEncryptType) {
                    newValue = EncryptContextHelp.encrypt(newValue);
                } else if (JsonEncryptType.DECRYPT == serializerJsonEncryptType) {
                    newValue = EncryptContextHelp.decrypt(value);
                }
            }
            jsonGenerator.writeString(newValue);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
