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
package cn.frank.ulp.console.controller.app;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MvcResult;

import cn.frank.ulp.common.entity.app.AppEntity;
import cn.frank.ulp.common.repository.app.AppRepository;
import cn.frank.ulp.support.security.authentication.AuthenticationProvider;
import cn.frank.ulp.support.security.authentication.WebAuthenticationDetails;
import cn.frank.ulp.support.security.userdetails.Application;
import cn.frank.ulp.support.security.userdetails.UserDetails;
import cn.frank.ulp.support.security.userdetails.UserType;
import cn.frank.ulp.support.testsupport.AbstractIntegrationTest;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 应用 controller 集成测试（list / create / delete）。
 *
 * <p>使用 template="FORM" —— 由 ulp-application-form 提供的 {@code FormStandardApplicationServiceImpl}
 * 处理，create 时不需要额外协议 fixture。</p>
 *
 * <p>每个测试方法在 {@code @Transactional} 内执行，结束自动回滚。</p>
 */
@ActiveProfiles("test")
class AppControllerIT extends AbstractIntegrationTest {

    private static final String BASE = "/api/v1/app";

    @Autowired
    private AppRepository       appRepository;

    @Autowired
    private ObjectMapper        objectMapper;

    @Test
    void listEmptyOrPaged() throws Exception {
        // 测试 DB 在 @Transactional 内是干净的（除了 RootOrganizationInitializer 的根组织，与应用无关）。
        // 应用列表分页接口要求至少有 pageNumber / pageSize 参数。
        mockMvc
            .perform(get(BASE + "/list").param("current", "1").param("pageSize", "10")
                .with(authentication(mockAdminAuthentication())))
            .andExpect(status().isOk()).andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.result.list").isArray())
            .andExpect(jsonPath("$.result.pagination").exists());
    }

    @Test
    void createForm() throws Exception {
        String payload = """
                {
                  "name": "测试表单应用",
                  "template": "FORM",
                  "icon": "data:image/svg+xml;base64,PHN2Zy8+",
                  "remark": "IT 测试"
                }
                """;

        MvcResult result = mockMvc
            .perform(post(BASE + "/create").with(authentication(mockAdminAuthentication()))
                .with(csrf()).contentType(MediaType.APPLICATION_JSON).content(payload))
            .andExpect(status().isOk()).andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.result.id").isNotEmpty()).andReturn();

        JsonNode body = objectMapper.readTree(result.getResponse().getContentAsString());
        String appId = body.path("result").path("id").asText();
        assertThat(appId).as("create 返回的 id").isNotBlank();

        // 数据库副作用：appRepository.findById(appId) 应能查到
        AppEntity entity = appRepository.findById(appId).orElseThrow();
        assertThat(entity.getName()).isEqualTo("测试表单应用");
        assertThat(entity.getTemplate()).isEqualTo("FORM");
    }

    @Test
    void deleteCreatedApp() throws Exception {
        // 先创建
        String payload = """
                {
                  "name": "待删表单应用",
                  "template": "FORM",
                  "remark": "待删"
                }
                """;
        MvcResult createResult = mockMvc
            .perform(post(BASE + "/create").with(authentication(mockAdminAuthentication()))
                .with(csrf()).contentType(MediaType.APPLICATION_JSON).content(payload))
            .andExpect(status().isOk()).andReturn();
        String appId = objectMapper.readTree(createResult.getResponse().getContentAsString())
            .path("result").path("id").asText();
        assertThat(appId).isNotBlank();

        // 删除
        mockMvc
            .perform(delete(BASE + "/delete/{id}", appId)
                .with(authentication(mockAdminAuthentication())).with(csrf()))
            .andExpect(status().isOk()).andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.result").value(true));

        // 数据库副作用：刚才那个 id 已被删除（实体 @SQLDelete 软删，findById 应返回 empty）
        assertThat(appRepository.findById(appId)).as("软删后 findById 应为空").isEmpty();
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
