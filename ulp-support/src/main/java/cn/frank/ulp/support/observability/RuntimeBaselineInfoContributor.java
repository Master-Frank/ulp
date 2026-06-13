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
package cn.frank.ulp.support.observability;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.boot.SpringBootVersion;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.core.SpringVersion;
import org.springframework.stereotype.Component;

import tools.jackson.core.json.JsonFactory;

/**
 * 暴露 ULP 运行时基线版本到 /actuator/info 的 runtime namespace。
 *
 * <p>由 openspec/specs/runtime-baseline/spec.md 锁定的关键依赖（Spring Boot 4 / Java 21 / Jackson 3）
 * 在运行时通过此 InfoContributor 自报，供运维核对实际加载的版本与 spec 是否一致。
 *
 * <p>示例输出（GET /actuator/info）：
 * <pre>
 * {
 *   "runtime": {
 *     "springBoot": "4.0.7",
 *     "spring": "7.0.4",
 *     "java": "21.0.4",
 *     "jackson": "3.1.4"
 *   }
 * }
 * </pre>
 *
 * <p>使用 {@code @Component} 直接注册而非 SupportAutoConfiguration {@code @Bean}：
 * 三个 deployable Application（UlpConsoleApplication/UlpPortalApplication/UlpOpenApiApplication）
 * 都位于 {@code cn.frank.ulp} 包根，{@code @SpringBootApplication} 默认 component scan 必然覆盖
 * {@code cn.frank.ulp.support.observability}，无需依赖 AutoConfiguration 链路。
 */
@Component
public class RuntimeBaselineInfoContributor implements InfoContributor {

    @Override
    public void contribute(Info.Builder builder) {
        Map<String, Object> runtime = new LinkedHashMap<>();
        runtime.put("springBoot", nullSafe(SpringBootVersion.getVersion()));
        runtime.put("spring", nullSafe(SpringVersion.getVersion()));
        runtime.put("java", nullSafe(System.getProperty("java.version")));
        runtime.put("jackson", nullSafe(JsonFactory.class.getPackage().getImplementationVersion()));
        builder.withDetail("runtime", runtime);
    }

    private static String nullSafe(String s) {
        return s != null ? s : "unknown";
    }
}
