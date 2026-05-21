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

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * @author TopIAM
 * Created by support@topiam.cn on 2022/12/22 21:53
 */
public class EncryptedJsonDeserializer extends JsonDeserializer<Object> {

    private final JsonEncryptType deserializerJsonEncryptType;

    public EncryptedJsonDeserializer(JsonEncryptType deserializer) {
        this.deserializerJsonEncryptType = deserializer;
    }

    @Override
    public Object deserialize(final JsonParser parser,
                              final DeserializationContext context) throws IOException {
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
