/*
 * ulp-console - United Login Platform
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
package cn.frank.ulp.console.controller.account;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.test.context.ActiveProfiles;

import cn.frank.ulp.common.entity.account.UserEntity;
import cn.frank.ulp.common.repository.account.UserRepository;
import cn.frank.ulp.support.security.authentication.AuthenticationProvider;
import cn.frank.ulp.support.security.authentication.WebAuthenticationDetails;
import cn.frank.ulp.support.security.userdetails.Application;
import cn.frank.ulp.support.security.userdetails.UserDetails;
import cn.frank.ulp.support.security.userdetails.UserType;
import cn.frank.ulp.support.testsupport.AbstractIntegrationTest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 用户 controller 集成测试（list / create / delete）。
 *
 * <p>组织 parentId 直接用 {@code "root"} —— {@code RootOrganizationInitializer} 在应用启动时自动 seed，
 * 无需 SQL fixture；用户挂到 root 组织下即可。</p>
 *
 * <p>密码取 {@code "TestPass123"} —— 满足默认密码策略
 * （{@link cn.frank.ulp.core.setting.PasswordPolicySettingConstants#PASSWORD_POLICY_DEFAULT_SETTINGS}：
 * 长度 6–20、复杂度 NONE、不查弱密码库、不查账号信息、不查非法序列、连续相同字符 ≤3）。
 * 不开启欢迎通知，避开邮件/短信副作用。</p>
 *
 * <p>每个测试方法在 {@code @Transactional} 内执行，结束自动回滚。</p>
 */
@ActiveProfiles("test")
class UserControllerIT extends AbstractIntegrationTest {

    private static final String BASE   = "/api/v1/user";
    private static final String ORG_ID = "root";

    @Autowired
    private UserRepository      userRepository;

    @Test
    void listEmptyOrPaged() throws Exception {
        // UserConverter.userPoConvertToUserListResult 在 page.getContent() 为空时返回 new Page<>()
        // （list/pagination 都不 set），Jackson 序列化为 {}。与 App/Organization converter 不一致，
        // 是历史 bug，本次 IT 基线只验证 list 端点能正常返回 200 + success=true，不强约束空集形状。
        mockMvc
            .perform(get(BASE + "/list").param("current", "1").param("pageSize", "10")
                .with(authentication(mockAdminAuthentication())))
            .andExpect(status().isOk()).andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.result").exists());
    }

    @Test
    void createValidUser() throws Exception {
        String payload = """
                {
                  "organizationId": "%s",
                  "username": "it-user-create",
                  "password": "TestPass123",
                  "email": "it-user-create@example.com",
                  "fullName": "集成测试用户"
                }
                """.formatted(ORG_ID);

        mockMvc
            .perform(post(BASE + "/create").with(authentication(mockAdminAuthentication()))
                .with(csrf()).contentType(MediaType.APPLICATION_JSON).content(payload))
            .andExpect(status().isOk()).andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.result").value(true));

        // 数据库副作用：username 应能查到
        Optional<UserEntity> entity = userRepository.findByUsername("it-user-create");
        assertThat(entity).as("create 后 findByUsername").isPresent();
        assertThat(entity.get().getEmail()).isEqualTo("it-user-create@example.com");
        assertThat(entity.get().getFullName()).isEqualTo("集成测试用户");
    }

    @Test
    void deleteCreatedUser() throws Exception {
        // 先创建一个用户
        String payload = """
                {
                  "organizationId": "%s",
                  "username": "it-user-delete",
                  "password": "TestPass123",
                  "email": "it-user-delete@example.com",
                  "fullName": "待删用户"
                }
                """.formatted(ORG_ID);
        mockMvc
            .perform(post(BASE + "/create").with(authentication(mockAdminAuthentication()))
                .with(csrf()).contentType(MediaType.APPLICATION_JSON).content(payload))
            .andExpect(status().isOk());

        String userId = userRepository.findByUsername("it-user-delete").orElseThrow().getId();

        // 删除
        mockMvc
            .perform(delete(BASE + "/delete/{id}", userId)
                .with(authentication(mockAdminAuthentication())).with(csrf()))
            .andExpect(status().isOk()).andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.result").value(true));

        // 数据库副作用：实体 @SQLDelete 软删，findById 应返回 empty
        assertThat(userRepository.findById(userId)).as("软删后 findById 应为空").isEmpty();
    }

    /**
     * 构造一个具备 ADMIN 权限的 mock Authentication。
     * 详细背景见 {@code OrganizationControllerIT#mockAdminAuthentication} 的注释。
     */
    private static UsernamePasswordAuthenticationToken mockAdminAuthentication() {
        UserDetails u = new UserDetails("test-admin-id", "test-admin", UserType.ADMIN, true, true,
            true, true, AuthorityUtils.NO_AUTHORITIES);
        Set<Application> apps = new HashSet<>();
        u.setApplications(apps);
        u.setUpdateTime(LocalDateTime.now());
        UsernamePasswordAuthenticationToken token = UsernamePasswordAuthenticationToken
            .authenticated(u, null, u.getAuthorities());
        WebAuthenticationDetails details = new WebAuthenticationDetails("127.0.0.1", "test-session",
            null, null, new AuthenticationProvider("portal", "test"), LocalDateTime.now());
        token.setDetails(details);
        return token;
    }
}
