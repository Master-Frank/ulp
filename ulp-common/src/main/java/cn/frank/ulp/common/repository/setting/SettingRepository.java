/*
 * ulp-common - United Login Platform
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
package cn.frank.ulp.common.repository.setting;

import java.util.List;
import java.util.Objects;

import org.jetbrains.annotations.NotNull;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn.frank.ulp.common.entity.setting.SettingEntity;
import cn.frank.ulp.support.context.ApplicationContextService;
import cn.frank.ulp.support.util.BeanUtils;
import static cn.frank.ulp.common.constant.SettingConstants.SETTING_CACHE_NAME;
import static cn.frank.ulp.support.repository.base.BaseEntity.LAST_MODIFIED_BY;
import static cn.frank.ulp.support.repository.base.BaseEntity.LAST_MODIFIED_TIME;

/**
 * 设置表 Repository 接口
 *
 * @author Frank Zhang
 */
@Repository
@CacheConfig(cacheNames = { SETTING_CACHE_NAME })
public interface SettingRepository extends JpaRepository<SettingEntity, String> {
    /**
     * 根据KEY查询
     *
     * @param name {@link String}
     * @return {@link SettingEntity}
     */
    @Cacheable(key = "#p0", unless = "#result==null")
    SettingEntity findByName(String name);

    /**
     * 根据KEY查询
     *
     * @param name {@link String}
     * @return {@link SettingEntity}
     */
    List<SettingEntity> findByNameLike(String name);

    /**
     * 根据类型查询安全配置
     *
     * @param names {@link List}
     * @return {@link SettingEntity}
     */
    List<SettingEntity> findByNameIn(List<String> names);

    /**
     * 根据ID删除
     *
     * @param id  {@link Long}
     */
    @Override
    @CacheEvict(allEntries = true)
    void deleteById(@NotNull String id);

    /**
     * 删除全部
     */
    @Override
    @CacheEvict(allEntries = true)
    void deleteAll();

    /**
     * 根据名称删除
     *
     * @param name {@link String}
     */
    @Modifying
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    void deleteByName(@Param("name") String name);

    /**
     * 保存配置
     *
     * @param list {@link List}
     * @return {@link Boolean}
     */
    default Boolean save(List<SettingEntity> list) {
        for (SettingEntity setting : list) {
            SettingEntity type = getSelf().findByName(setting.getName());
            if (Objects.isNull(type)) {
                save(setting);
                continue;
            }
            BeanUtils.merge(setting, type, LAST_MODIFIED_BY, LAST_MODIFIED_TIME);
            save(type);
        }
        return true;
    }

    /**
     * 保存
     *
     * @param entity must not be {@literal null}.
     * @return {@link S}
     * @param <S> SettingEntity
     */
    @NotNull
    @Override
    @CacheEvict(allEntries = true)
    <S extends SettingEntity> S save(@NotNull S entity);

    default SettingRepository getSelf() {
        return ApplicationContextService.getBean(SettingRepository.class);
    }
}
