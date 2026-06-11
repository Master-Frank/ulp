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

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn.frank.ulp.support.security.userdetails.Application;
import cn.frank.ulp.support.security.userdetails.DataOrigin;
import cn.frank.ulp.support.security.userdetails.Group;
import cn.frank.ulp.support.security.userdetails.Organization;
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
        JsonNode authoritiesNode = unwrapTyped(node.get("authorities"));
        if (authoritiesNode != null && authoritiesNode.isArray()) {
            Iterator<JsonNode> it = authoritiesNode.elements();
            while (it.hasNext()) {
                JsonNode authNode = it.next();
                String authType = textOrNull(authNode, "type");
                String authValue = textOrNull(authNode, "authority");
                if (authValue != null) {
                    authorities.add(new cn.frank.ulp.support.security.core.GrantedAuthority(
                        authType != null ? authType : "ROLE", authValue));
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

        Set<Group> groups = readSet(node, "groups", mapper, Group.class);
        if (groups != null) {
            userDetails.setGroups(groups);
        }
        Set<Organization> organizations = readSet(node, "organizations", mapper,
            Organization.class);
        if (organizations != null) {
            userDetails.setOrganizations(organizations);
        }
        Set<Application> applications = readSet(node, "applications", mapper, Application.class);
        if (applications != null) {
            userDetails.setApplications(applications);
        }

        LocalDateTime lastUpdatePwd = readLocalDateTime(node.get("lastUpdatePasswordTime"));
        if (lastUpdatePwd != null) {
            userDetails.setLastUpdatePasswordTime(lastUpdatePwd);
        }
        LocalDateTime updateTime = readLocalDateTime(node.get("updateTime"));
        if (updateTime != null) {
            userDetails.setUpdateTime(updateTime);
        }
        LocalDate expireDate = readLocalDate(node.get("expireDate"));
        if (expireDate != null) {
            userDetails.setExpireDate(expireDate);
        }
        JsonNode dataOriginNode = node.get("dataOrigin");
        if (dataOriginNode != null && !dataOriginNode.isNull() && dataOriginNode.isObject()) {
            userDetails.setDataOrigin(mapper.treeToValue(dataOriginNode, DataOrigin.class));
        }

        return userDetails;
    }

    /**
     * LocalDateTime 在不带 JavaTimeModule 的 mapper 下默认序列化为
     * [year, month, day, hour, minute, second, nano] 数字数组（尾部 0 可能被省略）。
     * 也兼容已经被字符串化（ISO-8601）的情况。
     */
    private static LocalDateTime readLocalDateTime(JsonNode node) {
        if (node == null || node.isNull()) {
            return null;
        }
        if (node.isArray() && node.size() >= 3) {
            int year = node.get(0).asInt();
            int month = node.get(1).asInt();
            int day = node.get(2).asInt();
            int hour = node.size() > 3 ? node.get(3).asInt() : 0;
            int minute = node.size() > 4 ? node.get(4).asInt() : 0;
            int second = node.size() > 5 ? node.get(5).asInt() : 0;
            int nano = node.size() > 6 ? node.get(6).asInt() : 0;
            return LocalDateTime.of(year, month, day, hour, minute, second, nano);
        }
        if (node.isTextual()) {
            return LocalDateTime.parse(node.asText());
        }
        return null;
    }

    /**
     * LocalDate 默认序列化为 [year, month, day] 数字数组，兼容 ISO-8601 字符串。
     */
    private static LocalDate readLocalDate(JsonNode node) {
        if (node == null || node.isNull()) {
            return null;
        }
        if (node.isArray() && node.size() >= 3) {
            return LocalDate.of(node.get(0).asInt(), node.get(1).asInt(), node.get(2).asInt());
        }
        if (node.isTextual()) {
            return LocalDate.parse(node.asText());
        }
        return null;
    }

    private static <T> Set<T> readSet(JsonNode node, String field, ObjectMapper mapper,
                                      Class<T> elementType) throws IOException {
        JsonNode unwrapped = unwrapTyped(node.get(field));
        if (unwrapped == null || !unwrapped.isArray()) {
            return null;
        }
        Set<T> result = new LinkedHashSet<>();
        for (JsonNode el : unwrapped) {
            T value = mapper.treeToValue(el, elementType);
            if (value != null) {
                result.add(value);
            }
        }
        return result;
    }

    /**
     * Spring SecurityJackson2Modules 强制 default typing 后，集合会被包成
     * ["java.util.Collections$UnmodifiableSet", [actual...]] 这种 2-element 数组。
     * 这里把外层 type 信息剥掉，返回真正的元素数组。
     */
    private static JsonNode unwrapTyped(JsonNode node) {
        if (node != null && node.isArray() && node.size() == 2 && node.get(0).isTextual()
            && node.get(1).isArray()) {
            return node.get(1);
        }
        return node;
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
