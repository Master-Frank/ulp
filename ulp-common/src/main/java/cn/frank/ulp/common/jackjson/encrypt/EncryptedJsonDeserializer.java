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

import org.apache.commons.lang3.StringUtils;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.ValueDeserializer;

/**
 * @author Frank Zhang
 */
public class EncryptedJsonDeserializer extends ValueDeserializer<Object> {

    private final JsonEncryptType deserializerJsonEncryptType;

    public EncryptedJsonDeserializer(JsonEncryptType deserializer) {
        this.deserializerJsonEncryptType = deserializer;
    }

    @Override
    public Object deserialize(final JsonParser parser,
                              final DeserializationContext context) throws JacksonException {
        String value = parser.getValueAsString();
        if (StringUtils.isBlank(value)) {
            return null;
        }
        if (JsonEncryptType.ENCRYPT == deserializerJsonEncryptType) {
            return EncryptContextHelp.encrypt(value);
        } else if (JsonEncryptType.DECRYPT == deserializerJsonEncryptType) {
            return EncryptContextHelp.decrypt(value);
        }
        return value;
    }
}
