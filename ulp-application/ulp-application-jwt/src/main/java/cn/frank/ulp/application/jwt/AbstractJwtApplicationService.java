/*
 * ulp-application-jwt - United Login Platform
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
package cn.frank.ulp.application.jwt;

import java.util.Objects;
import java.util.Optional;

import cn.frank.ulp.application.AbstractCertApplicationService;
import cn.frank.ulp.application.exception.AppCertNotExistException;
import cn.frank.ulp.application.exception.AppNotExistException;
import cn.frank.ulp.application.jwt.model.JwtProtocolConfig;
import cn.frank.ulp.common.entity.app.AppCertEntity;
import cn.frank.ulp.common.entity.app.po.AppJwtConfigPO;
import cn.frank.ulp.common.enums.app.AppCertUsingType;
import cn.frank.ulp.common.repository.app.*;

/**
 * JWT 应用配置
 *
 * @author Frank Zhang
 */
public abstract class AbstractJwtApplicationService extends AbstractCertApplicationService
                                                    implements JwtApplicationService {

    /**
     * AppCertRepository
     */
    protected final AppCertRepository      appCertRepository;
    /**
     * ApplicationRepository
     */
    protected final AppRepository          appRepository;

    protected final AppJwtConfigRepository appJwtConfigRepository;

    @Override
    public void delete(String appId) {
        //删除应用
        appRepository.deleteById(appId);
        //删除应用账户
        appAccountRepository.deleteAllByAppId(appId);
        // 删除应用配置
        appJwtConfigRepository.deleteByAppId(appId);
        // 删除证书
        appCertRepository.deleteByAppId(appId);
    }

    @Override
    public JwtProtocolConfig getProtocolConfig(String appCode) {
        AppJwtConfigPO configPo = appJwtConfigRepository.findByAppCode(appCode);
        if (Objects.isNull(configPo)) {
            throw new AppNotExistException();
        }
        Optional<AppCertEntity> entity = appCertRepository
            .findByAppIdAndUsingType(configPo.getAppId(), AppCertUsingType.JWT_ENCRYPT);
        if (entity.isEmpty()) {
            throw new AppCertNotExistException();
        }

        JwtProtocolConfig.JwtProtocolConfigBuilder<?, ?> jwtProtocolConfig = JwtProtocolConfig
            .builder();

        //@formatter:off
        jwtProtocolConfig.appId(String.valueOf(configPo.getAppId()))
            .clientId(configPo.getClientId())
            .appName(configPo.getAppName())
            .clientSecret(configPo.getClientSecret())
            .appCode(configPo.getAppCode())
            .appTemplate(configPo.getAppTemplate())
            .redirectUrl(configPo.getRedirectUrl())
            .targetLinkUrl(configPo.getTargetLinkUrl())
            .bindingType(configPo.getBindingType())
            .idTokenTimeToLive(Objects.toString(configPo.getIdTokenTimeToLive().toSeconds(),""))
            .idTokenSubjectType(configPo.getIdTokenSubjectType())
            .configured(configPo.getConfigured());

        entity.ifPresent(appCert -> {
            jwtProtocolConfig.jwtPrivateKey(appCert.getPrivateKey());
            jwtProtocolConfig.jwtPublicKey(appCert.getPublicKey());
        });
        //@formatter:on
        return jwtProtocolConfig.build();
    }

    protected AbstractJwtApplicationService(AppJwtConfigRepository appJwtConfigRepository,
                                            AppGroupAssociationRepository appGroupAssociationRepository,
                                            AppCertRepository appCertRepository,
                                            AppRepository appRepository,
                                            AppAccountRepository appAccountRepository,
                                            AppAccessPolicyRepository appAccessPolicyRepository) {
        super(appCertRepository, appGroupAssociationRepository, appAccountRepository,
            appAccessPolicyRepository, appRepository);
        this.appCertRepository = appCertRepository;
        this.appRepository = appRepository;
        this.appJwtConfigRepository = appJwtConfigRepository;
    }
}
