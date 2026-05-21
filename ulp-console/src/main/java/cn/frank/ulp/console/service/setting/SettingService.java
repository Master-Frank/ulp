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
package cn.frank.ulp.console.service.setting;

import java.util.List;

import cn.frank.ulp.common.entity.setting.SettingEntity;

/**
 * @author TopIAM
 * Created by support@topiam.cn on 2021/11/9 22:30
 */
public interface SettingService {
    /**
     * 保存设置
     *
     * @param entity {@link SettingEntity}
     * @return {@link Boolean}
     */
    Boolean saveSetting(SettingEntity entity);

    /**
     * 根据KEY获取配置
     *
     * @param name {@link String}
     * @return {@link SettingEntity}
     */
    SettingEntity getSetting(String name);

    /**
     * 根据KEY模糊获取配置
     *
     * @param name {@link String}
     * @return {@link List<SettingEntity>}
     */
    List<SettingEntity> findByNameLike(String name);

    /**
     * 移除配置
     *
     * @param name {@link String}
     * @return {@link Boolean}
     */
    Boolean removeSetting(String name);
}
