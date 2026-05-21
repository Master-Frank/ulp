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
package cn.frank.ulp.common.repository.authentication;

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

import cn.frank.ulp.common.entity.authn.IdentityProviderEntity;

/**
 * <p>
 * 身份认证源配置 Repository  接口
 * </p>
 *
 * @author Frank Zhang
 */
@Repository
@CacheConfig(cacheNames = "idp")
public interface IdentityProviderRepository extends JpaRepository<IdentityProviderEntity, String>,
                                            JpaSpecificationExecutor<IdentityProviderEntity> {
    /**
     * 根据平台类型查询认证源配置
     *
     * @param type {@link String}
     * @return {@link IdentityProviderEntity}
     */
    List<IdentityProviderEntity> findByType(String type);

    /**
     * 根据平台类型查询是否显示
     *
     * @return {@link List}
     */
    List<IdentityProviderEntity> findByEnabledIsTrueAndDisplayedIsTrue();

    /**
     * 查询启用的社交认证源
     *
     * @return {@link List}
     */
    List<IdentityProviderEntity> findByEnabledIsTrue();

    /**
     * save
     *
     * @param entity {@link S}
     * @param <S>    {@link S}
     * @return {@link IdentityProviderEntity}
     */
    @NotNull
    @Override
    @CacheEvict(allEntries = true)
    <S extends IdentityProviderEntity> S save(@NotNull S entity);

    /**
     * Deletes the entity with the given id.
     *
     * @param id must not be {@literal null}.
     * @throws IllegalArgumentException in case the given {@literal id} is {@literal null}
     */
    @Override
    @CacheEvict(allEntries = true)
    void deleteById(@NotNull String id);

    /**
     * Retrieves an entity by its id.
     *
     * @param id must not be {@literal null}.
     * @return the entity with the given id or {@literal Optional#empty()} if none found.
     * @throws IllegalArgumentException if {@literal id} is {@literal null}.
     */
    @NotNull
    @Override
    @Cacheable(key = "#a0")
    Optional<IdentityProviderEntity> findById(@NotNull @Param(value = "id") String id);

    /**
     * 更新社交认证源状态
     *
     * @param id {@link  String}
     * @param enabled {@link  Boolean}
     * @return {@link  Boolean}
     */
    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @CacheEvict(allEntries = true)
    @Query(value = "UPDATE IdentityProviderEntity SET enabled=:enabled where id=:id")
    Integer updateIdentityProviderStatus(@Param(value = "id") String id,
                                         @Param(value = "enabled") Boolean enabled);

    /**
     * 根据ID查找，并且为启用
     *
     * @param id {@link String}
     * @return {@link IdentityProviderEntity}
     */
    @Cacheable(key = "#a0", unless = "#result == null")
    Optional<IdentityProviderEntity> findByIdAndEnabledIsTrue(String id);

    /**
     * 根据code查找，并且为启用
     *
     * @param code {@link Long}
     * @return {@link IdentityProviderEntity}
     */
    @Cacheable(key = "#a0", unless = "#result == null")
    Optional<IdentityProviderEntity> findByCodeAndEnabledIsTrue(String code);
}
