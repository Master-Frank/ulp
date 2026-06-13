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

import java.sql.Connection;
import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.stereotype.Component;

import liquibase.Liquibase;
import liquibase.changelog.ChangeSet;
import liquibase.changelog.RanChangeSet;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.integration.spring.SpringLiquibase;
import liquibase.resource.ClassLoaderResourceAccessor;

/**
 * 检测 Liquibase changelog 在运行时是否与数据库实际状态发生漂移。
 *
 * <p>触发场景：
 * <ul>
 *   <li>{@code unexpected}：DB 中存在已执行的 changeset，但 classpath 当前 changelog 没有该条目
 *       —— 通常是有人本地 update 后又把 changelog 文件回滚；或部署回滚但 DB 没回滚</li>
 *   <li>{@code unrun}：classpath 中存在 changeset 但 DB 没跑过 —— 理论上 SpringLiquibase
 *       启动期 {@code update()} 会跑掉，运行时出现意味着启动期 update 部分失败或被人为绕过</li>
 * </ul>
 *
 * <p>checksum 漂移（DB 行的 MD5SUM 与 classpath 计算结果不一致）由 SpringLiquibase 在 boot 期
 * 直接 fail-fast，应用根本起不来，故 indicator 不再重复检测以避免引入 classpath 反解析复杂度。
 *
 * <p>5 分钟内存缓存避免 health probe 高频触发时反复连 DB + parse classpath。
 *
 * <p>状态：
 * <pre>
 *   UP             —— 无 unexpected 且无 unrun
 *   OUT_OF_SERVICE —— 有 unexpected 或 unrun（运维需介入但服务流量可降级而非停摆）
 *   DOWN           —— Liquibase SDK 调用炸（DB 连不上/parse failure），细节在 error
 * </pre>
 *
 * <p>bean 名 {@code liquibaseChangelogDrift}（不是 indicator name 默认推导出来的
 * {@code liquibaseChangelog}），是为了让 /actuator/health components 里出现的 key
 * 更直白指出"漂移检测"语义；Spring Boot 4 actuator 用 {@code @Component} 注册的
 * HealthIndicator bean 名（去 {@code HealthIndicator} 后缀）作为 components key。
 */
@Component("liquibaseChangelogDriftHealthIndicator")
@ConditionalOnBean(SpringLiquibase.class)
public class LiquibaseChangelogHealthIndicator implements HealthIndicator {

    private static final Logger                 LOGGER    = LoggerFactory
        .getLogger(LiquibaseChangelogHealthIndicator.class);

    private static final Duration               CACHE_TTL = Duration.ofMinutes(5);

    private final SpringLiquibase               springLiquibase;

    private final DataSource                    dataSource;

    private final AtomicReference<CachedHealth> cache     = new AtomicReference<>();

    public LiquibaseChangelogHealthIndicator(SpringLiquibase springLiquibase,
                                             DataSource dataSource) {
        this.springLiquibase = springLiquibase;
        this.dataSource = dataSource;
    }

    @Override
    public Health health() {
        CachedHealth current = cache.get();
        if (current != null && current.isFresh()) {
            return current.health;
        }
        Health fresh = compute();
        cache.set(new CachedHealth(fresh, Instant.now()));
        return fresh;
    }

    /**
     * 仅供测试：清缓存以便强制重算。
     */
    void invalidateCache() {
        cache.set(null);
    }

    protected Health compute() {
        String changelogPath = stripClasspathPrefix(springLiquibase.getChangeLog());
        try (Connection conn = dataSource.getConnection()) {
            Database database = DatabaseFactory.getInstance()
                .findCorrectDatabaseImplementation(new JdbcConnection(conn));
            applyChangelogTableOverrides(database, springLiquibase);
            try (Liquibase liquibase = new Liquibase(changelogPath,
                new ClassLoaderResourceAccessor(), database)) {
                List<ChangeSet> unrun = liquibase.listUnrunChangeSets(null, null);
                Collection<RanChangeSet> unexpected = liquibase.listUnexpectedChangeSets(null,
                    null);
                return buildHealth(unrun, unexpected, springLiquibase.getChangeLog());
            }
        } catch (Exception e) {
            LOGGER.warn("liquibase changelog drift check failed", e);
            return Health.down(e).withDetail("changelog", springLiquibase.getChangeLog()).build();
        }
    }

    /**
     * 应用必须读取 SpringLiquibase 同款的自定义 changelog 表名（本仓的
     * {@code ulp_changelog_table} / {@code ulp_changelog_lock_table}）；否则新建的
     * {@link Liquibase} 会去查默认 {@code DATABASECHANGELOG}，那张表是空的，
     * 导致所有 changeset 都被报为"unrun"——彻底假阳性。
     */
    private static void applyChangelogTableOverrides(Database database,
                                                     SpringLiquibase springLiquibase) {
        if (springLiquibase.getDatabaseChangeLogTable() != null) {
            database.setDatabaseChangeLogTableName(springLiquibase.getDatabaseChangeLogTable());
        }
        if (springLiquibase.getDatabaseChangeLogLockTable() != null) {
            database
                .setDatabaseChangeLogLockTableName(springLiquibase.getDatabaseChangeLogLockTable());
        }
    }

    /**
     * SpringLiquibase 接受 Spring 资源前缀（{@code classpath:} / {@code classpath*:}），
     * 但 Liquibase 自身的 {@code ChangeLogParser} 只识别 plain path —— 它把整个字符串当文件路径
     * 去 {@link ClassLoaderResourceAccessor} 查，前缀里那个冒号会让它解析为 "file not found"。
     * 这里复刻 SpringLiquibase 内部归一化逻辑。
     */
    static String stripClasspathPrefix(String path) {
        if (path == null) {
            return null;
        }
        if (path.startsWith("classpath*:")) {
            return path.substring("classpath*:".length());
        }
        if (path.startsWith("classpath:")) {
            return path.substring("classpath:".length());
        }
        return path;
    }

    /**
     * 提取为静态方法以便单元测试在不启动 DB 的情况下直接验证健康响应构造逻辑。
     */
    static Health buildHealth(List<?> unrun, Collection<?> unexpected, String changelog) {
        if (unexpected.isEmpty() && unrun.isEmpty()) {
            return Health.up().withDetail("changelog", changelog)
                .withDetail("status", "all changesets applied").build();
        }
        Health.Builder builder = Health.outOfService().withDetail("changelog", changelog);
        if (!unexpected.isEmpty()) {
            builder.withDetail("unexpectedInDb",
                unexpected.stream().map(Object::toString).toList());
        }
        if (!unrun.isEmpty()) {
            builder.withDetail("unrunInClasspath", unrun.stream().map(Object::toString).toList());
        }
        return builder.build();
    }

    private static final class CachedHealth {

        private final Health  health;

        private final Instant computedAt;

        CachedHealth(Health health, Instant computedAt) {
            this.health = health;
            this.computedAt = computedAt;
        }

        boolean isFresh() {
            return Duration.between(computedAt, Instant.now()).compareTo(CACHE_TTL) < 0;
        }
    }
}
