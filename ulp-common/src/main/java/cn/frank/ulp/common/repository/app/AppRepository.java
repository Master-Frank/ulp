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
package cn.frank.ulp.common.repository.app;

import java.util.List;
import java.util.Optional;

import org.jetbrains.annotations.NotNull;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn.frank.ulp.common.entity.app.AppEntity;
import static cn.frank.ulp.common.constant.AppConstants.APP_CACHE_NAME;

/**
 * @author TopIAM
 */
@Repository
@CacheConfig(cacheNames = { APP_CACHE_NAME })
public interface AppRepository extends JpaRepository<AppEntity, String>,
                               JpaSpecificationExecutor<AppEntity>, AppRepositoryCustomized {

    /**
     * save
     *
     * @param entity must not be {@literal null}.
     * @param <S>    {@link S}
     * @return {@link AppEntity}
     */
    @NotNull
    @Override
    @CacheEvict(allEntries = true)
    <S extends AppEntity> S save(@NotNull S entity);

    /**
     * 更新应用状态
     *
     * @param id {@link  String}
     * @param enabled {@link  Boolean}
     * @return {@link  Boolean}
     */
    @Modifying
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    @Query(value = "UPDATE AppEntity SET enabled = :enabled WHERE id = :id")
    Integer updateAppStatus(@Param(value = "id") String id,
                            @Param(value = "enabled") Boolean enabled);

    /**
     * delete
     *
     * @param id must not be {@literal null}.
     */
    @Override
    @CacheEvict(allEntries = true)
    void deleteById(@NotNull String id);

    /**
     * find by id
     *
     * @param id must not be {@literal null}.
     * @return {@link AppEntity}
     */
    @NotNull
    @Override
    @Cacheable(key = "#p0", unless = "#result==null")
    Optional<AppEntity> findById(@NotNull @Param(value = "id") String id);

    /**
     * 根据clientId获取配置
     *
     * @param clientId {@link String}
     * @return {@link AppEntity}
     */
    @Cacheable(key = "#p0", unless = "#result==null")
    Optional<AppEntity> findByClientId(String clientId);

    /**
     *  findByCode
     *
     * @param appCode {@link String}
     * @return {@link AppEntity}
     */
    @NotNull
    @Cacheable(key = "#p0", unless = "#result==null")
    Optional<AppEntity> findByCode(String appCode);

    /**
     *  findByIdIn
     *
     * @param appIds {@link List<String>}
     * @return {@link List<AppEntity>}
     */
    @NotNull
    List<AppEntity> findByIdIn(List<String> appIds);
}
