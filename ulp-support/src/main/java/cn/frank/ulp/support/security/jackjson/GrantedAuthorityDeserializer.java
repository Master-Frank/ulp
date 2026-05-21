/*
 * ulp-support - ULP support library (replaces the former eiam-support private jar).
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
package cn.frank.ulp.support.security.jackjson;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import cn.frank.ulp.support.security.core.GrantedAuthority;

public class GrantedAuthorityDeserializer extends JsonDeserializer<GrantedAuthority> {

    @Override
    public GrantedAuthority deserialize(JsonParser jp,
                                        DeserializationContext ctxt) throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);
        JsonNode typeNode = node.get("type");
        JsonNode authorityNode = node.get("authority");
        String type = typeNode == null || typeNode.isNull() ? "" : typeNode.asText();
        String authority = authorityNode == null || authorityNode.isNull() ? ""
            : authorityNode.asText();
        return new GrantedAuthority(type, authority);
    }
}
