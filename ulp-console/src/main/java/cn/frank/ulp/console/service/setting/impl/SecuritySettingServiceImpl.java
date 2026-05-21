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
import java.util.concurrent.Executor;

import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.frank.ulp.common.entity.setting.SettingEntity;
import cn.frank.ulp.common.repository.setting.SettingRepository;
import cn.frank.ulp.console.converter.setting.SecuritySettingConverter;
import cn.frank.ulp.console.pojo.result.setting.SecurityBasicConfigResult;
import cn.frank.ulp.console.pojo.save.setting.SecurityBasicSaveParam;
import cn.frank.ulp.console.service.setting.SecuritySettingService;
import cn.frank.ulp.core.security.session.Session;
import cn.frank.ulp.support.context.ApplicationContextService;
import cn.frank.ulp.support.context.ServletContextService;
import static cn.frank.ulp.common.constant.ConfigBeanNameConstants.DEFAULT_SECURITY_FILTER_CHAIN;
import static cn.frank.ulp.core.setting.SecuritySettingConstants.SECURITY_BASIC_KEY;

/**
 * <p>
 * 安全设置表 服务实现类
 * </p>
 *
 * @author Frank Zhang
 */
@Service
public class SecuritySettingServiceImpl extends SettingServiceImpl
                                        implements SecuritySettingService {

    /**
     * 获取基础配置
     *
     * @return {@link SecurityBasicConfigResult}
     */
    @Override
    public SecurityBasicConfigResult getBasicConfig() {
        //查询数据库配置
        List<SettingEntity> list = settingRepository.findByNameIn(SECURITY_BASIC_KEY);
        return securitySettingConverter.entityConvertToSecurityBasicConfigResult(list);
    }

    /**
     * 保存基础配置
     *
     * @param param {@link SecurityBasicSaveParam}
     * @return {@link Boolean}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveBasicConfig(SecurityBasicSaveParam param) {

        //删除密码配置
        SECURITY_BASIC_KEY.forEach(settingRepository::deleteByName);
        //保存
        List<SettingEntity> list = securitySettingConverter
            .securityBasicSaveParamConvertToEntity(param);
        Boolean save = settingRepository.save(list);
        String currentSessionId = ServletContextService.getSession().getId();
        //异步下线所有用户（排除当前操作用户）
        executor.execute(() -> {
            List<Object> principals = sessionRegistry.getAllPrincipals();
            principals.forEach(i -> {
                if (i instanceof Session) {
                    if (!((Session) i).getSessionId().equals(currentSessionId)) {
                        sessionRegistry.getSessionInformation(((Session) i).getSessionId())
                            .expireNow();
                    }
                }
            });
        });
        // refresh
        ApplicationContextService.refresh(DEFAULT_SECURITY_FILTER_CHAIN);
        return save;
    }

    /**
     * SecurityBasicDataConverter
     */
    private final SecuritySettingConverter securitySettingConverter;

    private final SettingRepository        settingRepository;

    private final SessionRegistry          sessionRegistry;

    private final Executor                 executor;

    public SecuritySettingServiceImpl(SettingRepository settingsRepository,
                                      SecuritySettingConverter securitySettingConverter,
                                      SettingRepository settingRepository,
                                      SessionRegistry sessionRegistry,
                                      AsyncConfigurer asyncConfigurer) {
        super(settingsRepository);
        this.securitySettingConverter = securitySettingConverter;
        this.settingRepository = settingRepository;
        this.sessionRegistry = sessionRegistry;
        this.executor = asyncConfigurer.getAsyncExecutor();
    }
}
