/*
 * eiam-console - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
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
 * @author TopIAM
 * Created by support@topiam.cn on 2021/11/9 22:29
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
