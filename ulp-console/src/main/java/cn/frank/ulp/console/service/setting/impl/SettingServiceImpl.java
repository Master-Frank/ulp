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
import java.util.Objects;

import cn.frank.ulp.common.entity.setting.SettingEntity;
import cn.frank.ulp.common.repository.setting.SettingRepository;
import cn.frank.ulp.console.service.setting.SettingService;
import cn.frank.ulp.support.util.BeanUtils;

import lombok.AllArgsConstructor;
import static cn.frank.ulp.support.repository.base.BaseEntity.LAST_MODIFIED_BY;
import static cn.frank.ulp.support.repository.base.BaseEntity.LAST_MODIFIED_TIME;

/**
 * 设置impl
 *
 * @author Frank Zhang
 */
@AllArgsConstructor
public class SettingServiceImpl implements SettingService {
    /**
     * 保存设置
     *
     * @param entity {@link SettingEntity}
     * @return {@link Boolean}
     */
    @Override
    public Boolean saveSetting(SettingEntity entity) {
        SettingEntity setting = getSetting(entity.getName());
        SettingEntity saveEntity = entity;
        // 当前配置存在，更改操作
        if (!Objects.isNull(setting)) {
            BeanUtils.merge(entity, setting, LAST_MODIFIED_BY, LAST_MODIFIED_TIME);
            saveEntity = setting;
        }
        settingsRepository.save(saveEntity);
        return Boolean.TRUE;
    }

    /**
     * 根据KEY获取配置
     *
     * @param name {@link String}
     * @return {@link SettingEntity}
     */
    @Override
    public SettingEntity getSetting(String name) {
        return settingsRepository.findByName(name);
    }

    /**
     * 根据KEY获取配置
     *
     * @param name {@link String}
     * @return {@link SettingEntity}
     */
    @Override
    public List<SettingEntity> findByNameLike(String name) {
        return settingsRepository.findByNameLike(name);
    }

    /**
     * 移除配置
     *
     * @param name {@link String}
     * @return {@link Boolean}
     */
    @Override
    public Boolean removeSetting(String name) {
        settingsRepository.deleteByName(name);
        return Boolean.TRUE;
    }

    /**
     * SettingRepository
     */
    private final SettingRepository settingsRepository;

}
