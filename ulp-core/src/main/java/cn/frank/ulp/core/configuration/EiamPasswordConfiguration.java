/*
 * ulp-core - United Login Platform
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
package cn.frank.ulp.core.configuration;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import cn.frank.ulp.common.constant.ConfigBeanNameConstants;
import cn.frank.ulp.common.repository.account.UserHistoryPasswordRepository;
import cn.frank.ulp.common.repository.account.UserRepository;
import cn.frank.ulp.common.repository.setting.SettingRepository;
import cn.frank.ulp.core.security.password.manager.DefaultPasswordPolicyManager;
import cn.frank.ulp.support.security.password.PasswordPolicyManager;
import cn.frank.ulp.support.security.password.generator.DefaultPasswordGenerator;
import cn.frank.ulp.support.security.password.weak.DefaultPasswordWeakLibImpl;
import cn.frank.ulp.support.security.password.weak.PasswordWeakLib;

/**
 * SecurityConfiguration
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2019/9/27 22:54
 */
@Configuration
public class EiamPasswordConfiguration {

    /**
     * 密码策略管理器
     *
     * @param userRepository {@link UserRepository} UserRepository
     * @param userHistoryPasswordRepository {@link UserHistoryPasswordRepository} 用户历史密码repository
     * @param settingRepository {@link SettingRepository} SettingRepository
     * @param passwordWeakLib               {@link PasswordWeakLib} PasswordWeakLib
     * @param passwordEncoder               {@link PasswordEncoder} PasswordEncoder
     * @return {@link  DefaultPasswordPolicyManager}
     */
    @Bean(ConfigBeanNameConstants.DEFAULT_PASSWORD_POLICY_MANAGER)
    @RefreshScope
    public PasswordPolicyManager passwordPolicyManager(UserRepository userRepository,
                                                       UserHistoryPasswordRepository userHistoryPasswordRepository,
                                                       SettingRepository settingRepository,
                                                       PasswordWeakLib passwordWeakLib,
                                                       PasswordEncoder passwordEncoder) {
        return new DefaultPasswordPolicyManager(userRepository, userHistoryPasswordRepository,
            settingRepository, passwordWeakLib, passwordEncoder);
    }

    /**
     * 默认密码生成
     *
     * @return {@link DefaultPasswordGenerator}
     */
    @Bean
    public DefaultPasswordGenerator defaultPasswordGenerator() {
        return new DefaultPasswordGenerator();
    }

    /**
     * 弱密码库
     *
     * @return {@link PasswordWeakLib}
     */
    @Bean
    public PasswordWeakLib passwordWeakLib() {
        return new DefaultPasswordWeakLibImpl();
    }

}
