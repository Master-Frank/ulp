/*
 * ulp-portal - United Login Platform
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
package cn.frank.ulp.portal.protocol.oidc;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.util.UriComponentsBuilder;

import cn.frank.ulp.support.security.authentication.AuthenticationProvider;
import cn.frank.ulp.support.security.authentication.WebAuthenticationDetails;
import cn.frank.ulp.support.security.userdetails.Application;
import cn.frank.ulp.support.security.userdetails.UserDetails;
import cn.frank.ulp.support.security.userdetails.UserType;
import cn.frank.ulp.support.testsupport.AbstractIntegrationTest;
import cn.frank.ulp.support.trace.TraceUtils;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * OIDC 授权码流程集成测试。
 *
 * <p>覆盖 4 条主链路：</p>
 * <ul>
 *   <li>{@link #happyPath()} —— 完整的 authorize → token 兑换链路</li>
 *   <li>{@link #invalidClientId()} —— 未注册 client_id 走 authorize 返回错误</li>
 *   <li>{@link #expiredAuthorizationCode()} —— 授权码 TTL=1ns 立即过期，token 兑换返回 invalid_grant</li>
 *   <li>{@link #mismatchedRedirectUri()} —— token 兑换时 redirect_uri 与 authorize 不一致，返回 invalid_grant</li>
 * </ul>
 *
 * <p>种子数据通过 {@link Sql @Sql} 在事务内加载，测试结束随事务回滚。</p>
 */
@ActiveProfiles("test")
@Sql(scripts = "/db/oidc-fixture.sql")
class OidcAuthorizationCodeFlowIT extends AbstractIntegrationTest {

    private static final String APP_CODE           = "test-oidc-app";
    private static final String EXPIRED_APP_CODE   = "test-oidc-expired";
    private static final String CLIENT_ID          = "test-client";
    private static final String CLIENT_SECRET      = "test-secret";
    private static final String EXPIRED_CLIENT     = "expired-client";
    private static final String EXPIRED_SECRET     = "expired-secret";
    private static final String REDIRECT_URI       = "http://localhost/callback";

    private static final String AUTH_ENDPOINT_TPL  = "/api/v1/authorize/%s/oauth2/auth";
    private static final String TOKEN_ENDPOINT_TPL = "/api/v1/authorize/%s/oauth2/token";

    /**
     * 模拟 {@link cn.frank.ulp.support.trace.TraceFilter} 行为：在 MDC 中放一个 TRACE_ID。
     *
     * <p>背景：审计链路（OAuth2AuthenticationSuccessEventListener → AuditEventPublish.publish →
     * new AuditEvent(TraceUtils.get(), ...)）会读 MDC 的 TRACE_ID；EventObject 拒绝 null source，
     * 测试上下文里 TraceFilter 未生效（filter 顺序或注册时机的问题，留 todo 单独排查），
     * 这里直接在 @BeforeEach 里手动塞一个 traceId 让审计链路跑通。</p>
     */
    @BeforeEach
    void primeTraceId() {
        TraceUtils.put("test-trace-" + System.nanoTime());
    }

    @AfterEach
    void clearTraceId() {
        TraceUtils.remove();
    }

    @Test
    void happyPath() throws Exception {
        String location = mockMvc
            .perform(get(String.format(AUTH_ENDPOINT_TPL, APP_CODE))
                .with(authentication(mockAuthentication())).queryParam("response_type", "code")
                .queryParam("client_id", CLIENT_ID).queryParam("redirect_uri", REDIRECT_URI)
                // openid + profile：profile 会触发 OAuth2TokenCustomizer 读 user.getUpdateTime()
                // 写 id_token 的 updated_at claim；本次随 UserDetailsDeserializer 修复 LocalDateTime
                // round-trip 一并覆盖（之前 Redis 反序列化丢字段 → NPE 的回归断言）。
                .queryParam("scope", "openid profile").queryParam("state", "state-happy"))
            .andExpect(status().is3xxRedirection()).andExpect(header().exists("Location"))
            .andReturn().getResponse().getHeader("Location");

        String code = extractQueryParam(location, "code");
        assertThat(code).as("authorize 应在 Location 中返回 code 参数").isNotBlank();

        mockMvc
            .perform(post(String.format(TOKEN_ENDPOINT_TPL, APP_CODE))
                .header("Authorization", basicAuth(CLIENT_ID, CLIENT_SECRET))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("grant_type", "authorization_code").param("code", code)
                .param("redirect_uri", REDIRECT_URI))
            .andExpect(status().isOk()).andExpect(jsonPath("$.access_token").isNotEmpty())
            .andExpect(jsonPath("$.token_type").value("Bearer"))
            .andExpect(jsonPath("$.id_token").isNotEmpty());
    }

    @Test
    void invalidClientId() throws Exception {
        mockMvc.perform(get(String.format(AUTH_ENDPOINT_TPL, APP_CODE))
            .with(authentication(mockAuthentication())).queryParam("response_type", "code")
            .queryParam("client_id", "does-not-exist").queryParam("redirect_uri", REDIRECT_URI)
            .queryParam("scope", "openid")).andExpect(result -> {
                int sc = result.getResponse().getStatus();
                assertThat(sc).as("未注册 client_id 不应签发 code（200 一定是错的）").isNotEqualTo(200);
                if (sc >= 300 && sc < 400) {
                    String loc = result.getResponse().getHeader("Location");
                    assertThat(loc).as("redirect Location").isNotNull();
                    assertThat(loc).doesNotContain("code=");
                }
            });
    }

    @Test
    void expiredAuthorizationCode() throws Exception {
        String location = mockMvc
            .perform(get(String.format(AUTH_ENDPOINT_TPL, EXPIRED_APP_CODE))
                .with(authentication(mockAuthentication())).queryParam("response_type", "code")
                .queryParam("client_id", EXPIRED_CLIENT).queryParam("redirect_uri", REDIRECT_URI)
                .queryParam("scope", "openid").queryParam("state", "state-expired"))
            .andExpect(status().is3xxRedirection()).andReturn().getResponse().getHeader("Location");

        String code = extractQueryParam(location, "code");
        assertThat(code).isNotBlank();

        // TTL=1s 是 Spring Auth Server TokenSettings 能接受的最短窗口；sleep 1500ms 远超 TTL。
        Thread.sleep(1500);

        mockMvc
            .perform(post(String.format(TOKEN_ENDPOINT_TPL, EXPIRED_APP_CODE))
                .header("Authorization", basicAuth(EXPIRED_CLIENT, EXPIRED_SECRET))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("grant_type", "authorization_code").param("code", code)
                .param("redirect_uri", REDIRECT_URI))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").value("invalid_grant"));
    }

    @Test
    void mismatchedRedirectUri() throws Exception {
        String location = mockMvc
            .perform(get(String.format(AUTH_ENDPOINT_TPL, APP_CODE))
                .with(authentication(mockAuthentication())).queryParam("response_type", "code")
                .queryParam("client_id", CLIENT_ID).queryParam("redirect_uri", REDIRECT_URI)
                .queryParam("scope", "openid"))
            .andExpect(status().is3xxRedirection()).andReturn().getResponse().getHeader("Location");

        String code = extractQueryParam(location, "code");
        assertThat(code).isNotBlank();

        mockMvc
            .perform(post(String.format(TOKEN_ENDPOINT_TPL, APP_CODE))
                .header("Authorization", basicAuth(CLIENT_ID, CLIENT_SECRET))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("grant_type", "authorization_code").param("code", code)
                .param("redirect_uri", "http://evil.example/callback"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").value("invalid_grant"));
    }

    /**
     * 构造一个挂载了两个测试应用的 mock 用户：
     * {@link cn.frank.ulp.protocol.oidc.context.OidcAuthorizationServerContextFilter} 的访问控制要求
     * {@code userDetails.applications} 包含当前 appCode，否则返回 403。
     */
    private static UserDetails mockUser() {
        UserDetails u = new UserDetails("test-user-id", "test-user", UserType.USER, true, true,
            true, true, AuthorityUtils.NO_AUTHORITIES);
        Set<Application> apps = new HashSet<>(
            List.of(new Application("test-app-id-1", APP_CODE, "Test OIDC App", null),
                new Application("test-app-id-2", EXPIRED_APP_CODE, "Test OIDC Expired", null)));
        u.setApplications(apps);
        // OIDC id_token 中的 updated_at claim 依赖 updateTime；不设置会在签发 id_token 时 NPE。
        u.setUpdateTime(LocalDateTime.now());
        return u;
    }

    /**
     * 构造带 {@link WebAuthenticationDetails details} 的 Authentication。
     *
     * <p>{@code SecurityMockMvcRequestPostProcessors.user(...)} 不会填充 details，但
     * {@code AuditEventPublish} 会监听 token issuance 事件并读取 {@code details.getAuthenticationProvider().getType()}，
     * details 为 null 会直接 NPE。这里手工建一份最小可用 details 来跑通审计链路。</p>
     */
    private static UsernamePasswordAuthenticationToken mockAuthentication() {
        UserDetails u = mockUser();
        UsernamePasswordAuthenticationToken token = UsernamePasswordAuthenticationToken
            .authenticated(u, null, u.getAuthorities());
        WebAuthenticationDetails details = new WebAuthenticationDetails("127.0.0.1", "test-session",
            null, null, new AuthenticationProvider("oauth2", "test"), LocalDateTime.now());
        token.setDetails(details);
        return token;
    }

    private static String basicAuth(String user, String pass) {
        String token = java.util.Base64.getEncoder()
            .encodeToString((user + ":" + pass).getBytes(java.nio.charset.StandardCharsets.UTF_8));
        return "Basic " + token;
    }

    private static String extractQueryParam(String url, String name) {
        if (url == null) {
            return null;
        }
        return UriComponentsBuilder.fromUri(URI.create(url)).build().getQueryParams()
            .getFirst(name);
    }
}
