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

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.frank.ulp.common.entity.setting.SettingEntity;
import cn.frank.ulp.common.repository.setting.SettingRepository;
import cn.frank.ulp.console.converter.setting.SecurityDefensePolicyConverter;
import cn.frank.ulp.console.pojo.result.setting.EmailProviderConfigResult;
import cn.frank.ulp.console.pojo.result.setting.SecurityDefensePolicyConfigResult;
import cn.frank.ulp.console.pojo.save.setting.SecurityDefensePolicyParam;
import cn.frank.ulp.console.service.setting.SecurityDefensePolicyService;
import cn.frank.ulp.support.context.ApplicationContextService;
import cn.frank.ulp.support.exception.TopIamException;
import cn.frank.ulp.support.security.util.ContentSecurityPolicyUtils;

import lombok.extern.slf4j.Slf4j;
import static cn.frank.ulp.common.constant.ConfigBeanNameConstants.DEFAULT_SECURITY_FILTER_CHAIN;
import static cn.frank.ulp.core.setting.SecuritySettingConstants.SECURITY_DEFENSE_POLICY_KEY;

/**
 * @author TopIAM
 * Created by support@topiam.cn on 2023-03-09
 */
@Slf4j
@Service
public class SecurityDefensePolicyServiceImpl extends SettingServiceImpl
                                              implements SecurityDefensePolicyService {

    /**
     * 创建品牌化
     *
     * @param param {@link SecurityDefensePolicyParam}
     * @return {@link Boolean}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveSecurityDefensePolicyConfig(SecurityDefensePolicyParam param) {
        String contentSecurityPolicy = param.getContentSecurityPolicy();
        try {
            ContentSecurityPolicyUtils.parse(contentSecurityPolicy);
        } catch (ContentSecurityPolicyUtils.ContentSecurityPolicyPolicyException e) {
            ContentSecurityPolicyUtils.PolicyError error = e.getError();
            log.error("解析内容安全策略异常：{}", error.toString());
            throw new TopIamException("内容安全策略格式错误");
        }
        //删除安全防御策略
        SECURITY_DEFENSE_POLICY_KEY.forEach(settingRepository::deleteByName);
        //保存
        List<SettingEntity> list = securityDefensePolicyConverter
            .securityDefensePolicyParamToEntity(param);
        Boolean save = settingRepository.save(list);
        ApplicationContextService.refresh(DEFAULT_SECURITY_FILTER_CHAIN);
        return save;
    }

    /**
     * 获取安全防御策略配置
     *
     * @return {@link EmailProviderConfigResult}
     */
    @Override
    public SecurityDefensePolicyConfigResult getSecurityPolicyConfig() {
        //内容安全策略
        List<SettingEntity> list = settingRepository.findByNameIn(SECURITY_DEFENSE_POLICY_KEY);
        return securityDefensePolicyConverter.entityToSecurityDefensePolicyConfigResult(list);
    }

    /**
     * SecurityPolicyConverter
     */
    private final SecurityDefensePolicyConverter securityDefensePolicyConverter;
    private final SettingRepository              settingRepository;

    /**
     * BrandServiceImpl
     *
     * @param settingRepository {@link SettingRepository}
     * @param securityDefensePolicyConverter  {@link SecurityDefensePolicyConverter}
     */
    public SecurityDefensePolicyServiceImpl(SettingRepository settingRepository,
                                            SecurityDefensePolicyConverter securityDefensePolicyConverter) {
        super(settingRepository);
        this.securityDefensePolicyConverter = securityDefensePolicyConverter;
        this.settingRepository = settingRepository;
    }
}
