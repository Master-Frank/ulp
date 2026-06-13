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
package cn.frank.ulp.portal.security;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import cn.frank.ulp.common.entity.account.UserEntity;
import cn.frank.ulp.common.enums.UserStatus;
import cn.frank.ulp.common.repository.account.UserRepository;
import cn.frank.ulp.support.testsupport.AbstractPasswordUpgradeIT;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

/**
 * Portal form login 链路的密码升级验证（普通用户库 {@link UserEntity}）。
 *
 * <p>Portal 的 DAP 在 {@code PortalSecurityConfiguration#daoAuthenticationProvider} 显式装配，
 * 并注入了 {@code UserDetailsPasswordServiceImpl}（操作 {@link UserRepository#updatePassword}），
 * 因此 form login 走完一次成功认证后 DB 密文应自动 rehash。</p>
 */
@ActiveProfiles("test")
class PasswordUpgradeIT extends AbstractPasswordUpgradeIT {

    @Autowired
    private UserRepository userRepository;

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
        return builder.with(csrf());
    }

    @Override
    protected String seedAccountWithBcryptPassword(String username, String rawPassword) {
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword(bcryptEncode(rawPassword));
        user.setStatus(UserStatus.ENABLED);
        user.setEmail(username + "@example.com");
        user.setFullName(username);
        user.setEmailVerified(Boolean.TRUE);
        user.setPhoneVerified(Boolean.FALSE);
        user.setNeedChangePassword(Boolean.FALSE);
        user.setDataOrigin("input");
        user.setLastUpdatePasswordTime(LocalDateTime.now());
        // saveAndFlush 强制立刻 INSERT；纯 save() 在 @Transactional 测试事务里会推迟到下次 flush，
        // 导致登录时 findByUsername 看不到 seed 账号 → 400 Bad credentials。
        return userRepository.saveAndFlush(user).getId();
    }

    @Override
    protected String findEncodedPasswordById(String accountId) {
        return userRepository.findById(accountId)
            .orElseThrow(() -> new IllegalStateException("seed user not found: " + accountId))
            .getPassword();
    }

    @Override
    protected void deleteAccountById(String accountId) {
        userRepository.deleteById(accountId);
    }
}
