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

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;
import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.Status;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * 单元测试覆盖三种核心场景：UP（无漂移）/ OUT_OF_SERVICE（有漂移）/ 缓存命中。
 *
 * <p>不 mock Liquibase + JDBC（mock {@link liquibase.database.DatabaseFactory} 静态调用 +
 * Liquibase 构造函数链路成本太高且脆）；改为两条路径：
 * <ol>
 *   <li>{@link LiquibaseChangelogHealthIndicator#buildHealth} 静态方法直测构造逻辑，
 *       覆盖 UP / OUT_OF_SERVICE / 仅 unrun / 仅 unexpected 几种排列</li>
 *   <li>用 anonymous 子类覆盖 {@code compute()} + 计数器，验证 5 分钟缓存确实拦截重复计算</li>
 * </ol>
 */
class LiquibaseChangelogHealthIndicatorTest {

    private static final String CHANGELOG = "classpath*:db/ulp-changelog-master.xml";

    @Test
    void buildHealth_upWhenNoDrift() {
        Health health = LiquibaseChangelogHealthIndicator.buildHealth(List.of(), List.of(),
            CHANGELOG);

        assertThat(health.getStatus()).isEqualTo(Status.UP);
        assertThat(health.getDetails()).containsEntry("changelog", CHANGELOG)
            .containsEntry("status", "all changesets applied");
    }

    @Test
    void buildHealth_outOfServiceWhenUnexpectedOnly() {
        Health health = LiquibaseChangelogHealthIndicator.buildHealth(List.of(),
            List.of("orphan-changeset-1"), CHANGELOG);

        assertThat(health.getStatus()).isEqualTo(Status.OUT_OF_SERVICE);
        assertThat(health.getDetails()).containsKey("unexpectedInDb")
            .doesNotContainKey("unrunInClasspath");
        assertThat(health.getDetails().get("unexpectedInDb"))
            .asInstanceOf(org.assertj.core.api.InstanceOfAssertFactories.LIST)
            .containsExactly("orphan-changeset-1");
    }

    @Test
    void buildHealth_outOfServiceWhenUnrunOnly() {
        Health health = LiquibaseChangelogHealthIndicator.buildHealth(List.of("pending-changeset"),
            List.of(), CHANGELOG);

        assertThat(health.getStatus()).isEqualTo(Status.OUT_OF_SERVICE);
        assertThat(health.getDetails()).containsKey("unrunInClasspath")
            .doesNotContainKey("unexpectedInDb");
        assertThat(health.getDetails().get("unrunInClasspath"))
            .asInstanceOf(org.assertj.core.api.InstanceOfAssertFactories.LIST)
            .containsExactly("pending-changeset");
    }

    @Test
    void buildHealth_outOfServiceWhenBoth() {
        Health health = LiquibaseChangelogHealthIndicator.buildHealth(List.of("a"), List.of("b"),
            CHANGELOG);

        assertThat(health.getStatus()).isEqualTo(Status.OUT_OF_SERVICE);
        assertThat(health.getDetails()).containsKeys("unrunInClasspath", "unexpectedInDb");
    }

    @Test
    void stripClasspathPrefix_handlesAllForms() {
        assertThat(LiquibaseChangelogHealthIndicator.stripClasspathPrefix("classpath*:db/x.xml"))
            .isEqualTo("db/x.xml");
        assertThat(LiquibaseChangelogHealthIndicator.stripClasspathPrefix("classpath:db/x.xml"))
            .isEqualTo("db/x.xml");
        assertThat(LiquibaseChangelogHealthIndicator.stripClasspathPrefix("db/x.xml"))
            .isEqualTo("db/x.xml");
        assertThat(LiquibaseChangelogHealthIndicator.stripClasspathPrefix(null)).isNull();
    }

    @Test
    void cacheHitsAvoidRecompute() {
        AtomicInteger computeCount = new AtomicInteger();
        LiquibaseChangelogHealthIndicator indicator = newIndicatorWithStub(computeCount,
            Health.up().build());

        Health first = indicator.health();
        Health second = indicator.health();
        Health third = indicator.health();

        assertThat(computeCount.get()).isEqualTo(1);
        assertThat(first).isSameAs(second).isSameAs(third);
    }

    @Test
    void invalidateCacheForcesRecompute() {
        AtomicInteger computeCount = new AtomicInteger();
        LiquibaseChangelogHealthIndicator indicator = newIndicatorWithStub(computeCount,
            Health.up().build());

        indicator.health();
        assertThat(computeCount.get()).isEqualTo(1);

        indicator.invalidateCache();
        indicator.health();
        assertThat(computeCount.get()).isEqualTo(2);
    }

    private static LiquibaseChangelogHealthIndicator newIndicatorWithStub(AtomicInteger counter,
                                                                          Health stubResult) {
        return new LiquibaseChangelogHealthIndicator(null, null) {
            @Override
            protected Health compute() {
                counter.incrementAndGet();
                return stubResult;
            }
        };
    }
}
