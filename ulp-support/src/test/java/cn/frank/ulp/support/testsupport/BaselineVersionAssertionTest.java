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
 * Before the Spring Boot 4 upgrade this test pins Boot 3.2.12 + Java 17, so that any
 * accidental dependency bump shows up as a test failure rather than as a silent runtime
 * surprise. Once Phase 1 lands, expectations get rewritten to Boot 4.0.5 + Java 21 and
 * the class gets renamed to {@code RuntimeBaselineAssertionTest} (see openspec change
 * {@code upgrade-spring-boot-4} task 10.2).
 */
class BaselineVersionAssertionTest {

    private static final String EXPECTED_BOOT_VERSION = "3.2.12";
    private static final String EXPECTED_JAVA_SPEC    = "17";

    @Test
    void springBootVersionMatchesBaseline() {
        String actual = SpringBootVersion.getVersion();
        assertEquals(EXPECTED_BOOT_VERSION, actual,
            "Spring Boot version drifted from pinned baseline; if intentional, update this test.");
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
