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
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MvcResult;

import cn.frank.ulp.common.entity.account.OrganizationEntity;
import cn.frank.ulp.common.repository.account.OrganizationRepository;
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
 * 组织架构 controller 集成测试（list / create / delete）。
 *
 * <p>list 用 {@code /get_root} + {@code /get_child} 组合（OrganizationController 没有 /list 端点）。
 * 根组织 id="root" 由 {@code RootOrganizationInitializer} 在应用启动时自动 seed，无需 SQL fixture。</p>
 *
 * <p>每个测试方法在 {@code @Transactional} 内执行，结束自动回滚 —— 跨方法互不污染。</p>
 */
@ActiveProfiles("test")
class OrganizationControllerIT extends AbstractIntegrationTest {

    private static final String    ROOT_ID = "root";
    private static final String    BASE    = "/api/v1/organization";

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private ObjectMapper           objectMapper;

    @Test
    void listGetRootAndChildren() throws Exception {
        // get_root —— 根组织应存在（启动时由 RootOrganizationInitializer seed）
        mockMvc.perform(get(BASE + "/get_root").with(authentication(mockAdminAuthentication())))
            .andExpect(status().isOk()).andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.result.id").value(ROOT_ID));

        // get_child of root —— 未 seed 任何子节点，应返回空数组（不报错）
        mockMvc
            .perform(get(BASE + "/get_child").param("parentId", ROOT_ID)
                .with(authentication(mockAdminAuthentication())))
            .andExpect(status().isOk()).andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.result").isArray());
    }

    @Test
    void createUnderRoot() throws Exception {
        String payload = """
                {
                  "code": "test-dept-create",
                  "parentId": "root",
                  "name": "测试部门",
                  "type": "department",
                  "enabled": true
                }
                """;

        mockMvc
            .perform(post(BASE + "/create").with(authentication(mockAdminAuthentication()))
                .with(csrf()).contentType(MediaType.APPLICATION_JSON).content(payload))
            .andExpect(status().isOk()).andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.result").value(true));

        // 数据库副作用：root 下面应有一条 name="测试部门" 的子组织
        List<OrganizationEntity> children = organizationRepository.findByParentId(ROOT_ID);
        assertThat(children).as("root 下应有新建的子组织")
            .anyMatch(o -> "测试部门".equals(o.getName()) && "test-dept-create".equals(o.getCode()));
    }

    @Test
    void deleteCreatedOrg() throws Exception {
        // 先 create 一个，拿到新生成的 id
        String payload = """
                {
                  "code": "test-dept-delete",
                  "parentId": "root",
                  "name": "待删部门",
                  "type": "department",
                  "enabled": true
                }
                """;
        mockMvc
            .perform(post(BASE + "/create").with(authentication(mockAdminAuthentication()))
                .with(csrf()).contentType(MediaType.APPLICATION_JSON).content(payload))
            .andExpect(status().isOk());

        String id = organizationRepository.findByParentId(ROOT_ID).stream()
            .filter(o -> "test-dept-delete".equals(o.getCode())).findFirst()
            .map(OrganizationEntity::getId).orElseThrow();

        MvcResult result = mockMvc
            .perform(delete(BASE + "/delete/{id}", id)
                .with(authentication(mockAdminAuthentication())).with(csrf()))
            .andExpect(status().isOk()).andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.result").value(true)).andReturn();

        JsonNode body = objectMapper.readTree(result.getResponse().getContentAsString());
        assertThat(body.path("result").asBoolean()).isTrue();

        // 数据库副作用：刚才那个 id 应已被删除
        assertThat(organizationRepository.findById(id)).as("删除后应查不到").isEmpty();
    }

    /**
     * 构造一个具备 ADMIN 权限的 mock Authentication。
     *
     * <p>必须用 {@code SecurityMockMvcRequestPostProcessors.authentication(...)} 而不是 {@code user(...)}，
     * 因为 {@code user(...)} 不会附加 {@link WebAuthenticationDetails details}，而审计监听器
     * （{@code AuditEventPublish}）会读 {@code details.getAuthenticationProvider().getType()}，
     * details 为 null 直接 NPE。这里手工建一份最小可用 details 让审计链路跑通。</p>
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
