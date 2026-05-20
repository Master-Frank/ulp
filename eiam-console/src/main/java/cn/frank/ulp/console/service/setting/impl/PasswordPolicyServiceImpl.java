/*
 * eiam-console - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
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
 * @author TopIAM
 * Created by support@topiam.cn on 2020-08-17
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
