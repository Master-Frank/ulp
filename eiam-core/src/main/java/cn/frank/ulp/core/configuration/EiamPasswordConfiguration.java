/*
 * eiam-core - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
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
