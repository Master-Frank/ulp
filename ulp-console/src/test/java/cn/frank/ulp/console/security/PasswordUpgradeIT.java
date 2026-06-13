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
package cn.frank.ulp.console.security;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import cn.frank.ulp.common.entity.setting.AdministratorEntity;
import cn.frank.ulp.common.enums.UserStatus;
import cn.frank.ulp.common.repository.setting.AdministratorRepository;
import cn.frank.ulp.support.testsupport.AbstractPasswordUpgradeIT;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

/**
 * Console form login 链路的密码升级验证。
 *
 * <p>seed 一条 {@code {bcrypt}} 密文的 {@link AdministratorEntity}，发一次 {@code POST /api/v1/login}
 * 后断言密文前缀升级到 {@code {argon2}}。Console 链路的 DAP 由
 * {@code ConsoleSecurityConfiguration#daoAuthenticationProvider} 显式装配并注入
 * {@code UserDetailsPasswordService}（admin 库）。</p>
 */
@ActiveProfiles("test")
class PasswordUpgradeIT extends AbstractPasswordUpgradeIT {

    @Autowired
    private AdministratorRepository administratorRepository;

    @Override
    protected String loginEndpoint() {
        return "/api/v1/login";
    }

    @Override
    protected MultiValueMap<String, String> loginParams(String username, String rawPassword) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("username", username);
        form.add("password", rawPassword);
        return form;
    }

    @Override
    protected MockHttpServletRequestBuilder customizeLoginRequest(MockHttpServletRequestBuilder builder) {
        // SpaCsrfTokenRequestHandler + XorCsrfTokenRequestAttributeHandler 要求请求带 _csrf 参数；
        // spring-security-test 的 csrf() postProcessor 会注入合法 token，绕过 ignoringRequestMatchers 之外的 CSRF 校验。
        return builder.with(csrf());
    }

    @Override
    protected String seedAccountWithBcryptPassword(String username, String rawPassword) {
        AdministratorEntity admin = new AdministratorEntity();
        admin.setUsername(username);
        admin.setPassword(bcryptEncode(rawPassword));
        admin.setStatus(UserStatus.ENABLED);
        admin.setEmail(username + "@example.com");
        admin.setEmailVerified(Boolean.TRUE);
        admin.setPhoneVerified(Boolean.FALSE);
        admin.setNeedChangePassword(Boolean.FALSE);
        admin.setLastUpdatePasswordTime(LocalDateTime.now());
        // saveAndFlush 强制立刻 INSERT；纯 save() 在 @Transactional 测试事务里会推迟到下次 flush，
        // 而 DaoAuthenticationProvider 的 findByUsername 不一定会触发 Hibernate auto-flush（缓存/derived
        // query 路径下尤其如此），导致登录时看不到 seed 账号 → 400 Bad credentials。
        return administratorRepository.saveAndFlush(admin).getId();
    }

    @Override
    protected String findEncodedPasswordById(String accountId) {
        // saveAndFlush 已在 seed 中触发；这里直接查；@CacheEvict(allEntries = true) 在每次 save/updatePassword 后清缓存,
        // 避免 cacheable findById 返回 stale。
        return administratorRepository.findById(accountId)
            .orElseThrow(() -> new IllegalStateException("seed account not found: " + accountId))
            .getPassword();
    }

    @Override
    protected void deleteAccountById(String accountId) {
        administratorRepository.deleteById(accountId);
    }
}
