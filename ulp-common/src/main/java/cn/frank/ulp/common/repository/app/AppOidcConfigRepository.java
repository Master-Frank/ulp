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
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn.frank.ulp.common.entity.app.AppOidcConfigEntity;
import cn.frank.ulp.common.entity.app.po.AppOidcConfigPO;
import static cn.frank.ulp.common.constant.AppConstants.OIDC_CONFIG_CACHE_NAME;

/**
 * @author TopIAM
 */
@Repository
@CacheConfig(cacheNames = { OIDC_CONFIG_CACHE_NAME })
public interface AppOidcConfigRepository extends JpaRepository<AppOidcConfigEntity, String> {
    /**
     * 按应用 ID 删除
     *
     * @param appId {@link String}
     */
    @CacheEvict(allEntries = true)
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    void deleteByAppId(@Param("appId") String appId);

    /**
     * delete
     *
     * @param id must not be {@literal null}.
     */
    @CacheEvict(allEntries = true)
    @Override
    void deleteById(@NotNull String id);

    /**
     * save
     *
     * @param entity must not be {@literal null}.
     * @param <S>    {@link S}
     * @return {@link AppOidcConfigEntity}
     */
    @NotNull
    @Override
    @CacheEvict(allEntries = true)
    <S extends AppOidcConfigEntity> S save(@NotNull S entity);

    /**
     * 根据应用ID获取配置
     *
     * @param appId {@link String}
     * @return {@link AppOidcConfigEntity}
     */
    Optional<AppOidcConfigEntity> findByAppId(String appId);

    /**
     * 根据应用ID获取
     *
     * @param appId {@link String}
     * @return {@link AppOidcConfigPO}
     */
    @Query(value = "SELECT NEW cn.frank.ulp.common.entity.app.po.AppOidcConfigPO(oc,a.code,a.name,a.template,a.clientId,a.clientSecret,a.initLoginUrl,a.authorizationType,a.enabled,a.configured) FROM AppEntity a INNER JOIN AppOidcConfigEntity oc ON a.id = oc.appId WHERE a.id =:appId")
    AppOidcConfigPO getByAppId(String appId);

    /**
     * 根据应用 Client 获取
     *
     * @param clientId {@link String}
     * @return {@link AppOidcConfigPO}
     */
    @Query(value = "SELECT NEW cn.frank.ulp.common.entity.app.po.AppOidcConfigPO(oc,a.code,a.name,a.template,a.clientId,a.clientSecret,a.initLoginUrl,a.authorizationType,a.enabled,a.configured) FROM AppEntity a INNER JOIN AppOidcConfigEntity oc ON a.id = oc.appId WHERE a.clientId =:clientId")
    AppOidcConfigPO getByClientId(String clientId);

    /**
     * 根据应用编码查询应用配置
     *
     * @param appCode {@link String}
     * @return {@link AppOidcConfigPO}
     */
    @Query(value = "SELECT NEW cn.frank.ulp.common.entity.app.po.AppOidcConfigPO(oc,a.code,a.name,a.template,a.clientId,a.clientSecret,a.initLoginUrl,a.authorizationType,a.enabled,a.configured) FROM AppEntity a INNER JOIN AppOidcConfigEntity oc ON a.id = oc.appId WHERE a.code =:appCode")
    AppOidcConfigPO findByAppCode(String appCode);
}
