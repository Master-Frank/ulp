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
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import cn.frank.ulp.common.entity.app.AppGroupEntity;
import static cn.frank.ulp.common.constant.AppConstants.APP_GROUP_CACHE_NAME_PREFIX;

/**
 * @author TopIAM
 */
@Repository
@CacheConfig(cacheNames = { APP_GROUP_CACHE_NAME_PREFIX })
public interface AppGroupRepository extends JpaRepository<AppGroupEntity, String>,
                                    AppGroupRepositoryCustomized {

    /**
     * save
     *
     * @param entity must not be {@literal null}.
     * @param <S>    {@link S}
     * @return {@link AppGroupEntity}
     */
    @NotNull
    @Override
    @CacheEvict(allEntries = true)
    <S extends AppGroupEntity> S save(@NotNull S entity);

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
     * @return {@link AppGroupEntity}
     */
    @NotNull
    @Override
    Optional<AppGroupEntity> findById(@NotNull String id);

    /**
     * 获取所有分组列表
     *
     * @return {@link List}
     */
    @Query(value = "FROM AppGroupEntity")
    List<AppGroupEntity> getAppGroupList();

    /**
     * 根据code查询
     *
     * @param code {@link String}
     * @return {@link AppGroupEntity}
     */
    Optional<AppGroupEntity> findByCode(@Param("code") String code);

    /**
     * 编码存在校验
     *
     * @param code {@link String}
     * @return {@link boolean}
     */
    boolean existsByCode(String code);
}
