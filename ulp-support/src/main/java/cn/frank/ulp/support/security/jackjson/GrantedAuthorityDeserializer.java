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
package cn.frank.ulp.support.security.jackjson;

import cn.frank.ulp.support.security.core.GrantedAuthority;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ValueDeserializer;

public class GrantedAuthorityDeserializer extends ValueDeserializer<GrantedAuthority> {

    @Override
    public GrantedAuthority deserialize(JsonParser jp,
                                        DeserializationContext ctxt) throws JacksonException {
        JsonNode node = ctxt.readTree(jp);
        JsonNode typeNode = node.get("type");
        JsonNode authorityNode = node.get("authority");
        String type = typeNode == null || typeNode.isNull() ? "" : typeNode.asText();
        String authority = authorityNode == null || authorityNode.isNull() ? ""
            : authorityNode.asText();
        return new GrantedAuthority(type, authority);
    }
}
