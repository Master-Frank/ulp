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

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * 三个部署单元（console / portal / 以及 ROPC 链路）共享的密码升级语义验证基类。
 *
 * <p>合同：
 * <ul>
 *   <li>默认 {@link PasswordEncoder} bean 来自 {@code PasswordEncoderFactories}，{@code encode()}
 *       产物必须以 {@code {argon2}} 前缀开头（即默认 encoder = argon2id）。</li>
 *   <li>给定一条 {@code {bcrypt}} 前缀的老密码 seed 账号，使用 raw 密码登录一次后，库里该账号的密文
 *       前缀必须翻转成 {@code {argon2}}（{@code DaoAuthenticationProvider} +
 *       {@code UserDetailsPasswordService} 自动 rehash）。</li>
 * </ul>
 *
 * <p>为避免 {@code ulp-support} 反向依赖 console/portal 的 repository / DTO，所有具体子类需要的
 * "登录端点 / 登录表单 / seed 账号 / 查密文" 操作都通过抽象模板方法暴露，由子类（住在
 * {@code ulp-console} 和 {@code ulp-portal} 的 test 目录）注入各自的 repository 实现。</p>
 *
 * <p>事务：本基类用 {@code @Transactional(propagation = NOT_SUPPORTED)} 覆盖父类
 * {@link AbstractIntegrationTest} 的 {@code @Transactional}，每个 test 方法不挂事务直接读写库。
 * 原因是 console 的 {@code AdministratorServiceImpl#findByUsernameOrPhoneOrEmail} 用
 * {@code CompletableFuture.supplyAsync} 在独立线程做 username/phone/email 三路并发查询，
 * 异步线程脱离测试主线程的事务上下文，因此看不见测试事务内未 commit 的 seed INSERT
 * —— 即使 {@code saveAndFlush} 强 flush 到 DB，事务可见性问题依然存在
 * （MySQL 默认 REPEATABLE_READ + 异步线程开独立 connection / 独立 snapshot）。
 * 改成 NOT_SUPPORTED 后 seed INSERT 走自身短事务立即 commit，所有线程都能读到；
 * 代价是 rehash 会真正改库，需通过 {@link #deleteAccountById(String)} 在 {@code @AfterEach}
 * 中手动清理 seed 账号，避免污染后续测试。</p>
 */
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public abstract class AbstractPasswordUpgradeIT extends AbstractIntegrationTest {

    /**
     * 当前 test 方法 seed 的账号 id，{@code @AfterEach} 阶段据此清理。
     */
    private String            seededAccountId;

    /**
     * Spring 上下文里装配的默认 {@link PasswordEncoder}（由
     * {@code cn.frank.ulp.support.security.crypto.password.PasswordEncoderFactories} 提供）。
     * 升级后默认 encoder id 应为 {@code argon2}。
     */
    @Autowired
    protected PasswordEncoder passwordEncoder;

    /**
     * 子类返回登录端点路径（如 {@code /api/v1/login}）。
     */
    protected abstract String loginEndpoint();

    /**
     * 子类构造登录请求的表单参数（含 username + password 字段 + 任何附加必填项，如验证码白名单字段）。
     *
     * @param username    seed 账号用户名
     * @param rawPassword 明文密码
     * @return form-data 参数 MultiValueMap
     */
    protected abstract MultiValueMap<String, String> loginParams(String username,
                                                                 String rawPassword);

    /**
     * 子类向库里插入一条密文为 {@code {bcrypt}xxx} 的 seed 账号，返回主键 id。
     *
     * @param username    用户名（基类保证唯一）
     * @param rawPassword 明文密码（基类登录时用同一个）
     * @return 新建账号 id
     */
    protected abstract String seedAccountWithBcryptPassword(String username, String rawPassword);

    /**
     * 子类按主键查询账号当前 stored encoded password（含 {@code {id}} 前缀）。
     *
     * @param accountId seed 阶段返回的 id
     * @return 当前数据库里的密文
     */
    protected abstract String findEncodedPasswordById(String accountId);

    /**
     * 子类按主键删除 seed 阶段创建的账号；由 {@code @AfterEach cleanup()} 调用，
     * 用于在 NOT_SUPPORTED 事务模式下避免测试 seed 污染后续测试。
     *
     * @param accountId seed 阶段返回的 id
     */
    protected abstract void deleteAccountById(String accountId);

    /**
     * 给子类一个钩子，登录请求发出前可加上 CSRF token、session 等。默认无操作。
     */
    protected MockHttpServletRequestBuilder customizeLoginRequest(MockHttpServletRequestBuilder builder) {
        return builder;
    }

    /**
     * 新账号 / 新密码：默认 encoder 出来的密文必须是 argon2id。
     */
    @Test
    void newAccountUsesArgon2Prefix() {
        String encoded = passwordEncoder.encode("any-raw-password");
        assertThat(encoded).as("默认 PasswordEncoder 必须把新密码编码为 {argon2}").startsWith("{argon2}");
        assertThat(passwordEncoder.matches("any-raw-password", encoded))
            .as("回校验：encode → matches 必须为 true").isTrue();
    }

    /**
     * 老 {@code {bcrypt}} 密文账号：登录一次后必须自动 rehash 成 {@code {argon2}}。
     */
    @Test
    void bcryptAccountUpgradesOnLogin() throws Exception {
        String username = "pwd-upgrade-" + System.nanoTime();
        String rawPassword = "Upgrade@Pwd-12345";

        seededAccountId = seedAccountWithBcryptPassword(username, rawPassword);

        String before = findEncodedPasswordById(seededAccountId);
        assertThat(before).as("seed 账号必须以 {bcrypt} 起头").startsWith("{bcrypt}");

        mockMvc.perform(customizeLoginRequest(
            post(loginEndpoint()).params(loginParams(username, rawPassword))));

        String after = findEncodedPasswordById(seededAccountId);
        assertThat(after).as("登录后密文前缀必须升级到 {argon2}").startsWith("{argon2}");
        assertThat(passwordEncoder.matches(rawPassword, after)).as("新 {argon2} 密文必须能通过 raw 密码校验")
            .isTrue();
    }

    /**
     * NOT_SUPPORTED 事务下没有自动回滚，seed 与 rehash 都真实写库；
     * 在每个 test 方法结束后按 id 清理，避免污染后续测试。
     */
    @AfterEach
    void cleanupSeededAccount() {
        if (seededAccountId != null) {
            try {
                deleteAccountById(seededAccountId);
            } finally {
                seededAccountId = null;
            }
        }
    }

    /**
     * 生成 {@code {bcrypt}xxx} 形式的密文，给子类 seed 用。固定 strength=10 与历史 admin 账号一致。
     */
    protected final String bcryptEncode(String rawPassword) {
        return "{bcrypt}" + new BCryptPasswordEncoder().encode(rawPassword);
    }
}
