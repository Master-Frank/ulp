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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import cn.frank.ulp.support.jackjson.SupportJackson2Module;
import cn.frank.ulp.support.security.core.GrantedAuthority;
import cn.frank.ulp.support.security.userdetails.Application;
import cn.frank.ulp.support.security.userdetails.DataOrigin;
import cn.frank.ulp.support.security.userdetails.Group;
import cn.frank.ulp.support.security.userdetails.Organization;
import cn.frank.ulp.support.security.userdetails.UserDetails;
import cn.frank.ulp.support.security.userdetails.UserType;

import tools.jackson.databind.ObjectMapper;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * 验证 UserDetailsDeserializer 在 Spring Session Redis 场景下能完整 round-trip。
 * 这是修复"反序列化时 ClassCastException"那条 bug 的回归测试，
 * 同时也验证 agent 怀疑的"丢字段"问题是否真存在。
 */
class UserDetailsRoundTripTest {

    private ObjectMapper buildSessionMapper() {
        // 与 ConsoleSecurityConfiguration.springSessionDefaultRedisSerializer 保持一致
        // SupportJackson2Module.objectMapperBuilder 已内置 Security 7 + SecurityJacksonModule
        return SupportJackson2Module.objectMapperBuilder(getClass().getClassLoader()).build();
    }

    private UserDetails buildFullUser() {
        UserDetails u = new UserDetails("id-001", "alice",
            "$2a$10$abcdefghijklmnopqrstuvwxyz0123456789ABCDEF", new UserType("USER", "User"), true,
            true, true, true, List.of(new GrantedAuthority("ROLE", "ROLE_USER")));
        u.setEmail("alice@ulp");
        u.setPhone("13800138000");
        u.setPhoneAreaCode("+86");
        u.setFullName("Alice 张");
        u.setNickName("Ali");
        u.setAvatar("https://x/a.png");
        u.setEmployeeNumber("E0001");
        u.setExternalId("ext-1");
        u.setPhoneVerified(true);
        u.setEmailVerified(false);
        u.setNeedChangePassword(false);

        Set<Group> groups = new LinkedHashSet<>();
        groups.add(new Group("g-1"));
        groups.add(new Group("g-2"));
        u.setGroups(groups);

        Set<Organization> orgs = new LinkedHashSet<>();
        orgs.add(new Organization("o-1", "/root/o-1"));
        u.setOrganizations(orgs);

        Set<Application> apps = new LinkedHashSet<>();
        apps.add(new Application("app-1", "code-1", "App One", null));
        u.setApplications(apps);

        u.setLastUpdatePasswordTime(LocalDateTime.of(2026, 1, 1, 0, 0));
        u.setUpdateTime(LocalDateTime.of(2026, 6, 10, 12, 34, 56));
        u.setExpireDate(LocalDate.of(2027, 12, 31));
        u.setDataOrigin(DataOrigin.INPUT);

        return u;
    }

    @Test
    void roundTripPreservesCoreFields() throws Exception {
        ObjectMapper mapper = buildSessionMapper();
        UserDetails original = buildFullUser();

        String json = mapper.writeValueAsString(original);
        UserDetails restored = mapper.readValue(json, UserDetails.class);

        assertThat(restored.getId()).isEqualTo("id-001");
        assertThat(restored.getUsername()).isEqualTo("alice");
        assertThat(restored.getPassword()).isEqualTo(original.getPassword());
        assertThat(restored.isEnabled()).isTrue();
        assertThat(restored.isAccountNonExpired()).isTrue();
        assertThat(restored.isCredentialsNonExpired()).isTrue();
        assertThat(restored.isAccountNonLocked()).isTrue();
    }

    @Test
    void roundTripPreservesUserType() throws Exception {
        ObjectMapper mapper = buildSessionMapper();
        UserDetails original = buildFullUser();

        String json = mapper.writeValueAsString(original);
        UserDetails restored = mapper.readValue(json, UserDetails.class);

        assertThat(restored.getUserType()).isNotNull();
        assertThat(restored.getUserType().getType()).isEqualTo("USER");
    }

    @Test
    void roundTripPreservesAuthorities() throws Exception {
        ObjectMapper mapper = buildSessionMapper();
        UserDetails original = buildFullUser();

        String json = mapper.writeValueAsString(original);
        UserDetails restored = mapper.readValue(json, UserDetails.class);

        assertThat(restored.getAuthorities()).hasSize(1);
        assertThat(restored.getAuthorities().iterator().next().getAuthority())
            .isEqualTo("ROLE_USER");
    }

    /**
     * 验证 agent 怀疑的"profile 字段丢失"。
     * 当前 UserDetailsDeserializer 只调 setEmail/setPhone/setFullName 等显式字段，
     * 不调 setLastUpdatePasswordTime / setApplications / setGroups / setOrganizations / setDataOrigin。
     * 此测试覆盖确实有 setter 调用的字段。
     */
    @Test
    void roundTripPreservesProfileFields() throws Exception {
        ObjectMapper mapper = buildSessionMapper();
        UserDetails original = buildFullUser();

        String json = mapper.writeValueAsString(original);
        UserDetails restored = mapper.readValue(json, UserDetails.class);

        assertThat(restored.getEmail()).isEqualTo("alice@ulp");
        assertThat(restored.getPhone()).isEqualTo("13800138000");
        assertThat(restored.getPhoneAreaCode()).isEqualTo("+86");
        assertThat(restored.getFullName()).isEqualTo("Alice 张");
        assertThat(restored.getNickName()).isEqualTo("Ali");
        assertThat(restored.getAvatar()).isEqualTo("https://x/a.png");
        assertThat(restored.getEmployeeNumber()).isEqualTo("E0001");
        assertThat(restored.getExternalId()).isEqualTo("ext-1");
        assertThat(restored.getPhoneVerified()).isTrue();
        assertThat(restored.getEmailVerified()).isFalse();
        assertThat(restored.getNeedChangePassword()).isFalse();
    }

    /**
     * groups / organizations / applications 必须 round-trip：
     * - portal UserServiceImpl 用 groups + organizations 做鉴权 subjectIds
     * - portal AppServiceImpl + 三个协议 ContextFilter 用 applications 决定可见应用
     */
    @Test
    void roundTripPreservesGroupsOrganizationsApplications() throws Exception {
        ObjectMapper mapper = buildSessionMapper();
        UserDetails original = buildFullUser();

        String json = mapper.writeValueAsString(original);
        UserDetails restored = mapper.readValue(json, UserDetails.class);

        assertThat(restored.getGroups()).hasSize(2);
        assertThat(restored.getGroups().stream().map(Group::getId)).containsExactlyInAnyOrder("g-1",
            "g-2");

        assertThat(restored.getOrganizations()).hasSize(1);
        assertThat(restored.getOrganizations().iterator().next().getId()).isEqualTo("o-1");

        assertThat(restored.getApplications()).hasSize(1);
        assertThat(restored.getApplications().iterator().next().getId()).isEqualTo("app-1");
    }

    /**
     * lastUpdatePasswordTime / updateTime / expireDate / dataOrigin 必须 round-trip。
     * 历史上 deserializer 丢掉了这几个字段，导致 OIDC token customizer 在 profile scope
     * 下读 user.getUpdateTime() 时 NPE（参见 OAuth2TokenCustomizer:88）。
     * 这里覆盖修复后的预期行为：写出去什么样、读回来就什么样。
     */
    @Test
    void roundTripPreservesTimestampAndDataOrigin() throws Exception {
        ObjectMapper mapper = buildSessionMapper();
        UserDetails original = buildFullUser();

        String json = mapper.writeValueAsString(original);
        UserDetails restored = mapper.readValue(json, UserDetails.class);

        assertThat(restored.getLastUpdatePasswordTime())
            .isEqualTo(LocalDateTime.of(2026, 1, 1, 0, 0));
        assertThat(restored.getUpdateTime()).isEqualTo(LocalDateTime.of(2026, 6, 10, 12, 34, 56));
        assertThat(restored.getExpireDate()).isEqualTo(LocalDate.of(2027, 12, 31));
        assertThat(restored.getDataOrigin()).isNotNull();
        assertThat(restored.getDataOrigin().getType()).isEqualTo("INPUT");
        assertThat(restored.getDataOrigin().getName()).isEqualTo("手动录入");
    }

    @Test
    void roundTripWithNullPasswordHandled() throws Exception {
        ObjectMapper mapper = buildSessionMapper();
        UserDetails u = new UserDetails("id-x", "bob", null, new UserType("USER", "User"), true,
            true, true, true, Collections.emptyList());

        String json = mapper.writeValueAsString(u);
        UserDetails restored = mapper.readValue(json, UserDetails.class);

        // 构造函数将 null password 替换为 ""
        assertThat(restored.getPassword()).isEqualTo("");
    }
}
