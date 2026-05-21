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

import java.util.Optional;

import org.jetbrains.annotations.NotNull;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import cn.frank.ulp.common.entity.app.AppCertEntity;
import cn.frank.ulp.common.entity.app.AppOidcConfigEntity;
import cn.frank.ulp.common.enums.app.AppCertUsingType;
import static cn.frank.ulp.common.constant.AppConstants.APP_CERT_CACHE_NAME;

/**
 * AppCertificateRepository
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/5/31 21:52
 */
@CacheConfig(cacheNames = { APP_CERT_CACHE_NAME })
public interface AppCertRepository extends JpaRepository<AppCertEntity, String> {
    /**
     * 根据应用ID查询证书
     *
     * @param appId     {@link String}
     * @param usingType {@link AppCertUsingType}
     * @return {@link AppCertEntity}
     */
    @Cacheable(key = "#p1.code+':'+#p0", unless = "#result==null")
    Optional<AppCertEntity> findByAppIdAndUsingType(String appId, AppCertUsingType usingType);

    /**
     * save
     *
     * @param entity must not be {@literal null}.
     * @param <S>    {@link S}
     * @return {@link AppCertEntity}
     */
    @NotNull
    @Override
    @CacheEvict(allEntries = true)
    <S extends AppCertEntity> S save(@NotNull S entity);

    /**
     * delete
     *
     * @param id must not be {@literal null}.
     */
    @CacheEvict(allEntries = true)
    @Override
    void deleteById(@NotNull String id);

    /**
     * 根据应用ID删除应用
     *
     * @param appId {@link String}
     */
    @CacheEvict(allEntries = true)
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    void deleteByAppId(@Param("appId") String appId);

    /**
     * find by id
     *
     * @param id must not be {@literal null}.
     * @return {@link AppOidcConfigEntity}
     */
    @NotNull
    @Override
    @Cacheable
    Optional<AppCertEntity> findById(@NotNull String id);
}
