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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * @author TopIAM
 * Created by support@topiam.cn on 2022/12/22 21:53
 */
public class EncryptionModule extends SimpleModule {

    private final JsonEncryptType serializer;
    private final JsonEncryptType deserializer;

    public EncryptionModule() {
        this.serializer = null;
        this.deserializer = null;
    }

    public EncryptionModule(JsonEncryptType serializer, JsonEncryptType deserializer) {
        this.serializer = serializer;
        this.deserializer = deserializer;
    }

    @Override
    public void setupModule(SetupContext setupContext) {
        setupContext.addBeanSerializerModifier(new EncryptedSerializerModifier(serializer));
        setupContext.addBeanDeserializerModifier(new EncryptedDeserializerModifier(deserializer));
    }

    public static ObjectMapper serializerEncrypt() {
        return createMapper(JsonEncryptType.ENCRYPT, JsonEncryptType.NONE);
    }

    public static ObjectMapper deserializerEncrypt() {
        return createMapper(JsonEncryptType.NONE, JsonEncryptType.ENCRYPT);
    }

    public static ObjectMapper serializerDecrypt() {
        return createMapper(JsonEncryptType.DECRYPT, JsonEncryptType.NONE);
    }

    public static ObjectMapper deserializerDecrypt() {
        return createMapper(JsonEncryptType.NONE, JsonEncryptType.DECRYPT);
    }

    public static ObjectMapper createMapper(JsonEncryptType serializer,
                                            JsonEncryptType deserializer) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.registerModule(new EncryptionModule(serializer, deserializer));
        return objectMapper;
    }
}
