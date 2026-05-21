/*
 * ulp-application-oidc - United Login Platform
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
package cn.frank.ulp.application.oidc;

import java.security.PrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;

import com.nimbusds.jose.jwk.RSAKey;

import cn.frank.ulp.application.AbstractCertApplicationService;
import cn.frank.ulp.application.exception.AppCertNotExistException;
import cn.frank.ulp.application.exception.AppNotExistException;
import cn.frank.ulp.application.oidc.model.OidcProtocolConfig;
import cn.frank.ulp.common.entity.app.AppCertEntity;
import cn.frank.ulp.common.entity.app.po.AppOidcConfigPO;
import cn.frank.ulp.common.repository.app.*;
import static cn.frank.ulp.common.enums.app.AppCertUsingType.OIDC_JWK;
import static cn.frank.ulp.support.util.CertUtils.readPrivateKey;
import static cn.frank.ulp.support.util.CertUtils.readPublicKey;

/**
 * OIDC 应用配置
 *
 * @author Frank Zhang
 */
public abstract class AbstractOidcApplicationService extends AbstractCertApplicationService
                                                     implements OidcApplicationService {

    @Override
    public void delete(String appId) {
        //删除应用
        appRepository.deleteById(appId);
        //删除证书
        appCertRepository.deleteByAppId(appId);
        //删除应用账户
        appAccountRepository.deleteAllByAppId(appId);
        //删除应用权限策略
        appAccessPolicyRepository.deleteAllByAppId(appId);
        //删除OIDC配置
        appOidcConfigRepository.deleteByAppId(appId);
    }

    /**
     * 获取协议配置
     *
     * @param appCode {@link String}
     * @return {@link OidcProtocolConfig}
     */
    @Override
    public OidcProtocolConfig getProtocolConfig(String appCode) {
        AppOidcConfigPO appConfig = appOidcConfigRepository.findByAppCode(appCode);
        if (Objects.isNull(appConfig)) {
            throw new AppNotExistException();
        }
        Optional<AppCertEntity> appCertOptional = appCertRepository
            .findByAppIdAndUsingType(appConfig.getAppId(), OIDC_JWK);
        if (appCertOptional.isEmpty()) {
            throw new AppCertNotExistException();
        }
        AppCertEntity appCert = appCertOptional.get();
        //@formatter:off
        try {

            PrivateKey rsaPrivateKey = readPrivateKey(appCert.getPrivateKey(), "");
            RSAPublicKey rsaPublicKey = (RSAPublicKey) readPublicKey(appCert.getPublicKey(), "");

            RSAKey rsaKey = new RSAKey.Builder(rsaPublicKey)
                    .privateKey(rsaPrivateKey)
                    .keyID(appCert.getId())
                    .build();

            return OidcProtocolConfig.builder()
                    .appId(appConfig.getAppId())
                    .appName(appConfig.getAppName())
                    .clientId(appConfig.getClientId())
                    .clientSecret(appConfig.getClientSecret())
                    .appCode(appConfig.getAppCode())
                    .appTemplate(appConfig.getAppTemplate())
                    .clientAuthMethods(appConfig.getClientAuthMethods())
                    .authGrantTypes(appConfig.getAuthGrantTypes())
                    .responseTypes(appConfig.getResponseTypes())
                    .redirectUris(appConfig.getRedirectUris())
                    .postLogoutRedirectUris(appConfig.getPostLogoutRedirectUris())
                    .grantScopes(appConfig.getGrantScopes())
                    .requireAuthConsent(appConfig.getRequireAuthConsent())
                    .requireProofKey(appConfig.getRequireProofKey())
                    .tokenEndpointAuthSigningAlgorithm(appConfig.getTokenEndpointAuthSigningAlgorithm())
                    .refreshTokenTimeToLive(appConfig.getRefreshTokenTimeToLive())
                    .authorizationCodeTimeToLive(appConfig.getAuthorizationCodeTimeToLive())
                    .deviceCodeTimeToLive(appConfig.getDeviceCodeTimeToLive())
                    .idTokenSignatureAlgorithm(appConfig.getIdTokenSignatureAlgorithm())
                    .idTokenTimeToLive(appConfig.getIdTokenTimeToLive())
                    .accessTokenFormat(appConfig.getAccessTokenFormat())
                    .accessTokenTimeToLive(appConfig.getAccessTokenTimeToLive())
                    .reuseRefreshToken(appConfig.getReuseRefreshToken())
                    .jwks(Collections.singletonList(rsaKey))
                    .configured(appConfig.getConfigured())
                    .build();

            //@formatter:on
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to select the JWK(s) -> " + ex.getMessage(),
                ex);
        }

    }

    /**
     * AppCertRepository
     */
    protected final AppCertRepository       appCertRepository;
    /**
     * ApplicationRepository
     */
    protected final AppRepository           appRepository;
    /**
     * AppOidcConfigRepository
     */
    protected final AppOidcConfigRepository appOidcConfigRepository;

    protected AbstractOidcApplicationService(AppCertRepository appCertRepository,
                                             AppGroupAssociationRepository appGroupAssociationRepository,
                                             AppAccountRepository appAccountRepository,
                                             AppAccessPolicyRepository appAccessPolicyRepository,
                                             AppRepository appRepository,
                                             AppOidcConfigRepository appOidcConfigRepository) {
        super(appCertRepository, appGroupAssociationRepository, appAccountRepository,
            appAccessPolicyRepository, appRepository);
        this.appCertRepository = appCertRepository;
        this.appRepository = appRepository;
        this.appOidcConfigRepository = appOidcConfigRepository;
    }
}
