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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn.frank.ulp.support.security.userdetails.UserDetails;
import cn.frank.ulp.support.security.userdetails.UserType;

public class UserDetailsDeserializer extends JsonDeserializer<UserDetails> {

    @Override
    public UserDetails deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        JsonNode node = mapper.readTree(jp);

        String id = textOrNull(node, "id");
        String username = textOrNull(node, "username");
        String password = textOrNull(node, "password");

        UserType userType = null;
        JsonNode userTypeNode = node.get("userType");
        if (userTypeNode != null && !userTypeNode.isNull()) {
            if (userTypeNode.isObject()) {
                userType = mapper.treeToValue(userTypeNode, UserType.class);
            } else {
                String typeText = userTypeNode.asText();
                userType = new UserType(typeText, typeText);
            }
        }

        boolean enabled = booleanOr(node, "enabled", true);
        boolean accountNonExpired = booleanOr(node, "accountNonExpired", true);
        boolean credentialsNonExpired = booleanOr(node, "credentialsNonExpired", true);
        boolean accountNonLocked = booleanOr(node, "accountNonLocked", true);

        List<GrantedAuthority> authorities = new ArrayList<>();
        JsonNode authoritiesNode = node.get("authorities");
        if (authoritiesNode != null && authoritiesNode.isArray()) {
            Iterator<JsonNode> it = authoritiesNode.elements();
            while (it.hasNext()) {
                JsonNode authNode = it.next();
                GrantedAuthority authority = mapper.treeToValue(authNode,
                    cn.frank.ulp.support.security.core.GrantedAuthority.class);
                if (authority != null) {
                    authorities.add(authority);
                }
            }
        }

        UserDetails userDetails = new UserDetails(id, username, password, userType, enabled,
            accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);

        applyOptionalString(node, "email", userDetails::setEmail);
        applyOptionalString(node, "phone", userDetails::setPhone);
        applyOptionalString(node, "phoneAreaCode", userDetails::setPhoneAreaCode);
        applyOptionalString(node, "fullName", userDetails::setFullName);
        applyOptionalString(node, "nickName", userDetails::setNickName);
        applyOptionalString(node, "avatar", userDetails::setAvatar);
        applyOptionalString(node, "employeeNumber", userDetails::setEmployeeNumber);
        applyOptionalString(node, "externalId", userDetails::setExternalId);

        applyOptionalBoolean(node, "phoneVerified", userDetails::setPhoneVerified);
        applyOptionalBoolean(node, "emailVerified", userDetails::setEmailVerified);
        applyOptionalBoolean(node, "needChangePassword", userDetails::setNeedChangePassword);

        return userDetails;
    }

    private static String textOrNull(JsonNode node, String field) {
        JsonNode v = node.get(field);
        return v == null || v.isNull() ? null : v.asText();
    }

    private static boolean booleanOr(JsonNode node, String field, boolean defaultValue) {
        JsonNode v = node.get(field);
        return v == null || v.isNull() ? defaultValue : v.asBoolean(defaultValue);
    }

    private static void applyOptionalString(JsonNode node, String field,
                                            java.util.function.Consumer<String> setter) {
        JsonNode v = node.get(field);
        if (v != null && !v.isNull()) {
            setter.accept(v.asText());
        }
    }

    private static void applyOptionalBoolean(JsonNode node, String field,
                                             java.util.function.Consumer<Boolean> setter) {
        JsonNode v = node.get(field);
        if (v != null && !v.isNull()) {
            setter.accept(v.asBoolean());
        }
    }
}
