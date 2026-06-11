/*
 * ulp-authentication-core - United Login Platform
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
package cn.frank.ulp.authentication.common.jackjson;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;

import cn.frank.ulp.authentication.common.authentication.OtpAuthentication;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ValueDeserializer;
import tools.jackson.databind.node.MissingNode;

/**
 * AuthenticationTokenDeserializer
 *
 * @author Frank Zhang
 */
@SuppressWarnings("DuplicatedCode")
class OtpAuthenticationTokenDeserializer extends ValueDeserializer<OtpAuthentication> {

    private static final TypeReference<List<GrantedAuthority>> GRANTED_AUTHORITY_LIST = new TypeReference<>() {
                                                                                      };

    private static final TypeReference<Object>                 OBJECT                 = new TypeReference<>() {
                                                                                      };

    @Override
    public OtpAuthentication deserialize(JsonParser jp,
                                         DeserializationContext ctxt) throws JacksonException {

        JsonNode jsonNode = ctxt.readTree(jp);
        boolean authenticated = readJsonNode(jsonNode, "authenticated").asBoolean();
        String recipient = readJsonNode(jsonNode, "recipient").asText();
        String type = readJsonNode(jsonNode, "type").asText();
        JsonNode principalNode = readJsonNode(jsonNode, "principal");
        Object principal = getPrincipal(ctxt, principalNode);
        //权限
        JavaType authoritiesType = ctxt.getTypeFactory().constructType(GRANTED_AUTHORITY_LIST);
        List<GrantedAuthority> authorities = ctxt
            .readTreeAsValue(readJsonNode(jsonNode, "authorities"), authoritiesType);
        OtpAuthentication authentication = (!authenticated)
            ? new OtpAuthentication(principal, recipient, type)
            : new OtpAuthentication(principal, recipient, type, authorities);
        JsonNode detailsNode = readJsonNode(jsonNode, "details");
        if (detailsNode.isNull() || detailsNode.isMissingNode()) {
            authentication.setDetails(null);
        } else {
            JavaType objectType = ctxt.getTypeFactory().constructType(OBJECT);
            Object details = ctxt.readTreeAsValue(detailsNode, objectType);
            authentication.setDetails(details);
        }
        return authentication;
    }

    private Object getPrincipal(DeserializationContext ctxt,
                                JsonNode principalNode) throws JacksonException {
        if (principalNode.isObject()) {
            return ctxt.readTreeAsValue(principalNode, Object.class);
        }
        return principalNode.asText();
    }

    private JsonNode readJsonNode(JsonNode jsonNode, String field) {
        return jsonNode.has(field) ? jsonNode.get(field) : MissingNode.getInstance();
    }

}
