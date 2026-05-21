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
package cn.frank.ulp.console.converter.setting;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ObjectUtils;
import org.mapstruct.Mapper;

import cn.frank.ulp.common.entity.setting.SettingEntity;
import cn.frank.ulp.console.pojo.result.setting.EmailProviderConfigResult;
import cn.frank.ulp.console.pojo.result.setting.SecurityDefensePolicyConfigResult;
import cn.frank.ulp.console.pojo.save.setting.SecurityDefensePolicyParam;
import static cn.frank.ulp.core.setting.SecuritySettingConstants.*;

/**
 * 安全防御策略映射
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023-03-09
 */
@Mapper(componentModel = "spring")
public interface SecurityDefensePolicyConverter {

    /**
     * param转实体类
     *
     * @param param {@link SecurityDefensePolicyParam}
     * @return {@link List<SettingEntity>}
     */
    default List<SettingEntity> securityDefensePolicyParamToEntity(SecurityDefensePolicyParam param) {
        //@formatter:off
        List<SettingEntity> list = new ArrayList<>();
        //内容防御策略
        if (ObjectUtils.isNotEmpty(param.getContentSecurityPolicy())) {
            list.add(new SettingEntity().setName(SECURITY_DEFENSE_POLICY_CONTENT_SECURITY_POLICY).setValue(String.valueOf(param.getContentSecurityPolicy())));
        }
        //连续登录失败持续时间
        if (ObjectUtils.isNotEmpty(param.getLoginFailureDuration())) {
            list.add(new SettingEntity().setName(SECURITY_DEFENSE_POLICY_LOGIN_FAILURE_DURATION).setValue(String.valueOf(param.getLoginFailureDuration())));
        }
        //连续登录失败次数
        if (ObjectUtils.isNotEmpty(param.getLoginFailureCount())) {
            list.add(new SettingEntity().setName(SECURITY_DEFENSE_POLICY_FAILURE_COUNT).setValue(String.valueOf(param.getLoginFailureCount())));
        }
        //自动解锁时间（分）
        if (ObjectUtils.isNotEmpty(param.getAutoUnlockTime())) {
            list.add(new SettingEntity().setName(SECURITY_DEFENSE_POLICY_AUTO_UNLOCK_TIME).setValue(String.valueOf(param.getAutoUnlockTime())));
        }
        return list;
    }

    /**
     * 实体转result
     *
     * @param list {@link List<SettingEntity>}
     * @return {@link EmailProviderConfigResult}
     */
    default SecurityDefensePolicyConfigResult entityToSecurityDefensePolicyConfigResult(List<SettingEntity> list) {
        //@formatter:off
        SecurityDefensePolicyConfigResult result = new SecurityDefensePolicyConfigResult();
        //转MAP
        Map<String, String> map = list.stream().collect(Collectors.toMap(SettingEntity::getName, SettingEntity::getValue, (key1, key2) -> key2));
        //内容安全策略
        result.setContentSecurityPolicy(map.containsKey(SECURITY_DEFENSE_POLICY_CONTENT_SECURITY_POLICY) ? map.get(SECURITY_DEFENSE_POLICY_CONTENT_SECURITY_POLICY) : SECURITY_DEFENSE_POLICY_DEFAULT_SETTINGS.get(SECURITY_DEFENSE_POLICY_CONTENT_SECURITY_POLICY));
        //自动解锁时间
        result.setAutoUnlockTime(Integer.valueOf(map.containsKey(SECURITY_DEFENSE_POLICY_AUTO_UNLOCK_TIME) ? map.get(SECURITY_DEFENSE_POLICY_AUTO_UNLOCK_TIME) : SECURITY_DEFENSE_POLICY_DEFAULT_SETTINGS.get(SECURITY_DEFENSE_POLICY_AUTO_UNLOCK_TIME)));
        //连续登录失败持续时间
        result.setLoginFailureDuration(Integer.valueOf(map.containsKey(SECURITY_DEFENSE_POLICY_LOGIN_FAILURE_DURATION) ? map.get(SECURITY_DEFENSE_POLICY_LOGIN_FAILURE_DURATION) : SECURITY_DEFENSE_POLICY_DEFAULT_SETTINGS.get(SECURITY_DEFENSE_POLICY_LOGIN_FAILURE_DURATION)));
        //连续登录失败次数
        result.setLoginFailureCount(Integer.valueOf(map.containsKey(SECURITY_DEFENSE_POLICY_FAILURE_COUNT) ? map.get(SECURITY_DEFENSE_POLICY_FAILURE_COUNT) : SECURITY_DEFENSE_POLICY_DEFAULT_SETTINGS.get(SECURITY_DEFENSE_POLICY_FAILURE_COUNT)));
        //@formatter:on
        return result;
    }
}
