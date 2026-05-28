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
package cn.frank.ulp.application.jwt.converter;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.text.StringSubstitutor;
import org.mapstruct.Mapper;

import cn.frank.ulp.application.jwt.pojo.AppJwtConfigGetResult;
import cn.frank.ulp.application.jwt.pojo.AppJwtProtocolEndpoint;
import cn.frank.ulp.application.jwt.pojo.AppJwtSaveConfigParam;
import cn.frank.ulp.common.entity.app.AppJwtConfigEntity;
import cn.frank.ulp.common.entity.app.po.AppJwtConfigPO;
import cn.frank.ulp.core.context.ContextService;
import static cn.frank.ulp.common.constant.ProtocolConstants.APP_CODE;
import static cn.frank.ulp.common.constant.ProtocolConstants.JwtEndpointConstants.JWT_SSO_PATH;

/**
 * 应用映射
 *
 * @author Frank Zhang
 */
@Mapper(componentModel = "spring")
public interface AppJwtConfigConverter {

    /**
     * save 转 entity
     *
     * @param config {@link AppJwtSaveConfigParam}
     * @return {@link AppJwtConfigEntity}
     */
    default AppJwtConfigEntity appJwtSaveConfigParamToEntity(AppJwtSaveConfigParam config) {
        if (config == null) {
            return null;
        }

        AppJwtConfigEntity entity = new AppJwtConfigEntity();
        entity.setRedirectUrl(config.getRedirectUrl());
        entity.setTargetLinkUrl(config.getTargetLinkUrl());
        entity.setBindingType(config.getBindingType());
        entity.setIdTokenSubjectType(config.getIdTokenSubjectType());
        entity.setIdTokenTimeToLive(Duration.ofSeconds(config.getIdTokenTimeToLive()));
        return entity;
    }

    /**
     * po 转 result
     *
     * @param po {@link AppJwtConfigPO}
     * @return {@link AppJwtConfigGetResult}
     */
    default AppJwtConfigGetResult entityConverterToFormConfigResult(AppJwtConfigPO po) {
        if (po == null) {
            return null;
        }
        AppJwtConfigGetResult result = new AppJwtConfigGetResult();
        if (po.getAppId() != null) {
            result.setAppId(po.getAppId());
        }
        result.setInitLoginUrl(po.getInitLoginUrl());
        result.setRedirectUrl(po.getRedirectUrl());
        result.setTargetLinkUrl(po.getTargetLinkUrl());
        result.setBindingType(po.getBindingType());
        result.setIdTokenSubjectType(po.getIdTokenSubjectType());
        result.setIdTokenTimeToLive(Objects.toString(po.getIdTokenTimeToLive().toSeconds(), ""));
        result.setProtocolEndpoint(getProtocolEndpointDomain(po.getAppCode()));
        return result;
    }

    /**
     * 获取协议端点
     *
     * @param appCode {@link String}
     * @return {@link AppJwtProtocolEndpoint}
     */
    private AppJwtProtocolEndpoint getProtocolEndpointDomain(String appCode) {
        //@formatter:off
        AppJwtProtocolEndpoint domain = new AppJwtProtocolEndpoint();
        Map<String,String> variables = new HashMap<>(16);
        variables.put(APP_CODE,appCode);
        StringSubstitutor sub = new StringSubstitutor(variables, "{", "}");
        //IDP SSO 端点
        domain.setIdpSsoEndpoint(sub.replace(ContextService.getPortalPublicBaseUrl()+JWT_SSO_PATH));
        return domain;
        //@formatter:on
    }
}
