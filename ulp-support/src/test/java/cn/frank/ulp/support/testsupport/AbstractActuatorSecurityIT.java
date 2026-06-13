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
package cn.frank.ulp.support.testsupport;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

/**
 * 三个部署单元（console / portal / openapi）共享的 actuator 安全语义验证。
 *
 * <p>三处的 actuator chain 都遵循相同的合同：
 * <ul>
 *   <li>health / info / prometheus 未鉴权 200</li>
 *   <li>env / loggers / metrics 等敏感端点未鉴权 401 或 403（console 是 hasRole("ADMIN")，
 *       portal/openapi 是 denyAll，对未鉴权请求语义等价）</li>
 *   <li>未在 {@code management.endpoints.web.exposure.include} 中列出的端点（heapdump）：
 *       security chain 拒绝 → 403（不是 404；security filter 在 dispatcher 之前运行，
 *       请求根本不到达端点路由层，所以"未注册导致 404"看不到。403 同样满足"不可访问"目标）</li>
 * </ul>
 *
 * <p>断言对状态码用 contains 而非 equals —— 包容 401（如果将来在某个 chain 上加 basic auth entry point）
 * 和 403（默认 deny / hasRole 未鉴权情况）两种合规结果。
 */
public abstract class AbstractActuatorSecurityIT extends AbstractIntegrationTest {

    @Test
    void healthEndpointIsPublic() throws Exception {
        mockMvc.perform(get("/actuator/health"))
            .andExpect(result -> assertThat(result.getResponse().getStatus())
                .as("/actuator/health 未鉴权应返回 200").isEqualTo(200));
    }

    @Test
    void infoEndpointIsPublic() throws Exception {
        mockMvc.perform(get("/actuator/info"))
            .andExpect(result -> assertThat(result.getResponse().getStatus())
                .as("/actuator/info 未鉴权应返回 200").isEqualTo(200));
    }

    @Test
    void prometheusEndpointIsPublic() throws Exception {
        // 先打一发 /actuator/health 让 WebMvcMetricsFilter 记录一条 http_server_requests 指标，
        // 然后 prometheus 拉取时才能在 body 里看到该计数器（否则首次抓取可能空）。
        mockMvc.perform(get("/actuator/health"));
        MvcResult result = mockMvc.perform(get("/actuator/prometheus")).andReturn();
        assertThat(result.getResponse().getStatus()).as("/actuator/prometheus 未鉴权应返回 200")
            .isEqualTo(200);
        String body = result.getResponse().getContentAsString();
        assertThat(body).as("prometheus exposition 必须包含 # HELP/# TYPE 头与至少一个 JVM 指标")
            .contains("# HELP", "# TYPE", "jvm_");
    }

    @Test
    void envEndpointRequiresAuth() throws Exception {
        mockMvc.perform(get("/actuator/env"))
            .andExpect(result -> assertThat(result.getResponse().getStatus())
                .as("/actuator/env 未鉴权应被拒（401/403）").isIn(401, 403));
    }

    @Test
    void loggersEndpointRequiresAuth() throws Exception {
        mockMvc.perform(get("/actuator/loggers"))
            .andExpect(result -> assertThat(result.getResponse().getStatus())
                .as("/actuator/loggers 未鉴权应被拒（401/403）").isIn(401, 403));
    }

    @Test
    void heapdumpIsNotAccessible() throws Exception {
        // heapdump 未在 management.endpoints.web.exposure.include 中暴露；理想情况下 actuator 没注册
        // 该端点路由，应返回 404。但 security chain 用 hasRole("ADMIN")/denyAll 兜底 /actuator/**，
        // 未鉴权请求会先在 ExceptionTranslationFilter 拒掉 → 403。两种都满足"不可访问"目标。
        mockMvc.perform(get("/actuator/heapdump"))
            .andExpect(result -> assertThat(result.getResponse().getStatus())
                .as("/actuator/heapdump 应不可访问（403 安全拒绝或 404 未暴露）").isIn(403, 404));
    }
}
