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
import org.springframework.boot.SpringBootVersion;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Pinned baseline assertion for the runtime stack.
 *
 * 锁死 Spring Boot 4.0.x + Java 21，任何非预期降级或升级都会让该测试失败。
 * 旧 BaselineVersionAssertionTest（pin 在 3.2.12 + 17）由 openspec change
 * {@code upgrade-spring-boot-4} task 10.2 重命名并切换断言为当前栈。
 */
class RuntimeBaselineAssertionTest {

    private static final String EXPECTED_BOOT_MAJOR_MINOR = "4.0";
    private static final String EXPECTED_JAVA_SPEC        = "21";

    @Test
    void springBootVersionMatchesBaseline() {
        String actual = SpringBootVersion.getVersion();
        assertTrue(actual != null && actual.startsWith(EXPECTED_BOOT_MAJOR_MINOR),
            "Spring Boot version drifted from pinned baseline (expected 4.0.x): " + actual);
    }

    @Test
    void javaSpecificationMatchesBaseline() {
        String actual = System.getProperty("java.specification.version");
        assertEquals(EXPECTED_JAVA_SPEC, actual,
            "Java specification version drifted from pinned baseline; if intentional, update this test.");
    }

    @Test
    void runtimeVersionLooksSane() {
        String runtime = System.getProperty("java.version");
        assertTrue(runtime != null && !runtime.isBlank(),
            "java.version system property must be set");
    }
}
