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
package cn.frank.ulp.console.service.setting.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.frank.ulp.common.constant.ConfigBeanNameConstants;
import cn.frank.ulp.common.entity.setting.SettingEntity;
import cn.frank.ulp.common.repository.setting.SettingRepository;
import cn.frank.ulp.console.converter.setting.PasswordPolicyConverter;
import cn.frank.ulp.console.pojo.result.setting.PasswordPolicyConfigResult;
import cn.frank.ulp.console.pojo.result.setting.WeakPasswordLibListResult;
import cn.frank.ulp.console.pojo.save.setting.PasswordPolicySaveParam;
import cn.frank.ulp.console.service.setting.PasswordPolicyService;
import cn.frank.ulp.support.context.ApplicationContextService;
import cn.frank.ulp.support.security.password.weak.PasswordWeakLib;
import static cn.frank.ulp.core.setting.PasswordPolicySettingConstants.PASSWORD_POLICY_KEYS;

/**
 * <p>
 * 密码策略 服务实现类
 * </p>
 *
 * @author Frank Zhang
 */
@Service
public class PasswordPolicyServiceImpl extends SettingServiceImpl implements PasswordPolicyService {

    /**
     * 获取配置
     *
     * @return {@link PasswordPolicyConfigResult}
     */
    @Override
    public PasswordPolicyConfigResult getPasswordPolicyConfig() {
        List<SettingEntity> list = settingRepository.findByNameIn(PASSWORD_POLICY_KEYS);
        return passwordPolicyConverter.entityConvertToPasswordPolicyConfigResult(list);
    }

    /**
     * 保存配置
     *
     * @param param {@link PasswordPolicySaveParam}
     * @return {@link Boolean}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean savePasswordPolicyConfig(PasswordPolicySaveParam param) {

        //删除密码配置
        PASSWORD_POLICY_KEYS.forEach(settingRepository::deleteByName);
        //保存
        List<SettingEntity> list = passwordPolicyConverter
            .passwordPolicySaveParamConvertToEntity(param);
        Boolean save = settingRepository.save(list);
        //refresh
        ApplicationContextService.refresh(ConfigBeanNameConstants.DEFAULT_PASSWORD_POLICY_MANAGER);
        return save;
    }

    /**
     * 获取系统弱密码库
     *
     * @return {@link WeakPasswordLibListResult}
     */
    @Override
    public List<WeakPasswordLibListResult> getWeakPasswordLibList() {
        List<WeakPasswordLibListResult> results = new ArrayList<>();
        List<String> list = passwordWeakLib.getWordList();
        for (String value : list) {
            results.add(new WeakPasswordLibListResult(value));
        }
        return results;
    }

    /**
     * 密码规则转换器
     */
    private final PasswordPolicyConverter passwordPolicyConverter;

    /**
     * SettingCipherTacticsRepository
     */
    private final SettingRepository       settingRepository;

    /**
     * PasswordWeakLib
     */
    private final PasswordWeakLib         passwordWeakLib;

    public PasswordPolicyServiceImpl(SettingRepository settingsRepository,
                                     PasswordPolicyConverter passwordPolicyConverter,
                                     SettingRepository settingRepository,
                                     PasswordWeakLib passwordWeakLib) {
        super(settingsRepository);
        this.passwordPolicyConverter = passwordPolicyConverter;
        this.settingRepository = settingRepository;
        this.passwordWeakLib = passwordWeakLib;
    }
}
