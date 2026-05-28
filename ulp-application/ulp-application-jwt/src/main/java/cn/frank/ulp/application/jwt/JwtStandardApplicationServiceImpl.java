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

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import cn.frank.ulp.application.exception.AppNotExistException;
import cn.frank.ulp.application.jwt.converter.AppJwtConfigConverter;
import cn.frank.ulp.application.jwt.pojo.AppJwtSaveConfigParam;
import cn.frank.ulp.audit.context.AuditContext;
import cn.frank.ulp.common.entity.app.AppEntity;
import cn.frank.ulp.common.entity.app.AppJwtConfigEntity;
import cn.frank.ulp.common.entity.app.po.AppJwtConfigPO;
import cn.frank.ulp.common.enums.app.*;
import cn.frank.ulp.common.repository.app.*;
import cn.frank.ulp.support.exception.UlpException;
import cn.frank.ulp.support.util.BeanUtils;
import cn.frank.ulp.support.validation.ValidationUtils;

import lombok.extern.slf4j.Slf4j;

import jakarta.validation.ConstraintViolationException;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

import static cn.frank.ulp.support.repository.base.BaseEntity.LAST_MODIFIED_BY;
import static cn.frank.ulp.support.repository.base.BaseEntity.LAST_MODIFIED_TIME;

/**
 * JWT 用户应用
 *
 * @author Frank Zhang
 */
@Component
@Slf4j
public class JwtStandardApplicationServiceImpl extends AbstractJwtApplicationService {

    /**
     * 更新应用配置
     *
     * @param appId {@link String}
     * @param config {@link Map}
     */
    @Override
    public void saveConfig(String appId, Map<String, Object> config) {
        AppJwtSaveConfigParam model;
        try {
            ObjectMapper mapper = new ObjectMapper();
            String value = mapper.writeValueAsString(config);
            // 指定序列化输入的类型
            mapper.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
            model = mapper.readValue(value, AppJwtSaveConfigParam.class);
        } catch (Exception e) {
            throw new UlpException(e.getMessage());
        }
        //@formatter:off
        ValidationUtils.ValidationResult<AppJwtSaveConfigParam> validationResult = ValidationUtils.validateEntity(model);
        if (validationResult.isHasErrors()) {
            throw new ConstraintViolationException(validationResult.getConstraintViolations());
        }
        //@formatter:on
        //1、修改基本信息
        Optional<AppEntity> optional = appRepository.findById(appId);
        if (optional.isEmpty()) {
            AuditContext.setContent("保存配置失败，应用 [" + appId + "] 不存在！");
            log.error(AuditContext.getContent());
            throw new AppNotExistException();
        }
        AppEntity appEntity = optional.get();
        appEntity.setInitLoginUrl(model.getInitLoginUrl());
        appEntity.setConfigured(true);
        appRepository.save(appEntity);
        //2、修改 JWT 配置
        Optional<AppJwtConfigEntity> jwt = appJwtConfigRepository.findByAppId(appId);
        if (jwt.isEmpty()) {
            AuditContext.setContent("保存配置失败，应用 [" + appId + "] 不存在！");
            log.error(AuditContext.getContent());
            throw new AppNotExistException();
        }
        AppJwtConfigEntity entity = jwt.get();
        AppJwtConfigEntity formConfig = appJwtConfigConverter.appJwtSaveConfigParamToEntity(model);
        BeanUtils.merge(formConfig, entity, LAST_MODIFIED_BY, LAST_MODIFIED_TIME);
        appJwtConfigRepository.save(entity);
    }

    /**
     * 获取配置
     *
     * @param appId {@link String}
     * @return {@link Map}
     */
    @Override
    public Object getConfig(String appId) {
        AppJwtConfigPO configPo = appJwtConfigRepository.getByAppId(appId);
        return appJwtConfigConverter.entityConverterToFormConfigResult(configPo);
    }

    /**
     * 获取应用标志
     *
     * @return {@link String}
     */
    @Override
    public String getCode() {
        return AppProtocol.JWT.getCode();
    }

    /**
     * 获取应用名称
     *
     * @return {@link String}
     */
    @Override
    public String getName() {
        return AppProtocol.JWT.getDesc();
    }

    /**
     * 获取应用描述
     *
     * @return {@link String}
     */
    @Override
    public String getDescription() {
        return "JWT（JSON Web Token）是在网络应用环境声明的一种基于 JSON 的开放标准。ULP 使用 JWT 进行分布式站点的单点登录 （SSO）。JWT 单点登录基于非对称加密，由 ULP 将用户状态和信息使用私钥加密，传递给应用后，应用使用公钥解密并进行验证。使用场景非常广泛，集成简单。";
    }

    /**
     * 获取应用类型
     *
     * @return {@link AppType}
     */
    @Override
    public AppType getType() {
        return AppType.STANDARD;
    }

    /**
     * 获取应用协议
     *
     * @return {@link AppProtocol}
     */
    @Override
    public AppProtocol getProtocol() {
        return AppProtocol.JWT;
    }

    /**
     * 获取base64图标
     *
     * @return {@link String}
     */
    @Override
    public String getBase64Icon() {
        return "data:image/svg+xml;base64,PHN2ZyB0PSIxNjYwOTc4MjExNDg3IiBjbGFzcz0iaWNvbiIgdmlld0JveD0iMCAwIDEwMjQgMTAyNCIgdmVyc2lvbj0iMS4xIiB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHAtaWQ9IjM4NzEiIHdpZHRoPSIyMDAiIGhlaWdodD0iMjAwIj48cGF0aCBkPSJNNTg5LjMxMiAyNzUuNDU2TDU4OC4yODggMGgtMTUzLjZsMS4wMjQgMjc1LjQ1NiA3Ni44IDEwNS40NzJ6IG0tMTUzLjYgNDcyLjA2NHYyNzYuNDhoMTUzLjZ2LTI3Ni40OEw1MTIuNTEyIDY0Mi4wNDh6IiBmaWxsPSIjRkZGRkZGIiBwLWlkPSIzODcyIj48L3BhdGg+PHBhdGggZD0iTTU4OS4zMTIgNzQ3LjUybDE2MS43OTIgMjIzLjIzMiAxMjMuOTA0LTkwLjExMi0xNjEuNzkyLTIyMy4yMzItMTIzLjkwNC0zOS45MzZ6IG0tMTUzLjYtNDcyLjA2NEwyNzIuODk2IDUyLjIyNGwtMTIzLjkwNCA5MC4xMTJMMzEwLjc4NCAzNjUuNTY4bDEyNC45MjggMzkuOTM2eiIgZmlsbD0iIzAwRjJFNiIgcC1pZD0iMzg3MyI+PC9wYXRoPjxwYXRoIGQ9Ik0zMTAuNzg0IDM2NS41NjhMNDguNjQgMjgwLjU3NiAxLjUzNiA0MjUuOTg0IDI2My42OCA1MTJsMTIzLjkwNC00MC45NnogbTMyNS42MzIgMTg2LjM2OGw3Ni44IDEwNS40NzIgMjYyLjE0NCA4NC45OTIgNDcuMTA0LTE0NS40MDgtMjYyLjE0NC04NC45OTJ6IiBmaWxsPSIjMDBCOUYxIiBwLWlkPSIzODc0Ij48L3BhdGg+PHBhdGggZD0iTTc2MC4zMiA1MTJsMjYyLjE0NC04Ni4wMTYtNDcuMTA0LTE0NS40MDhMNzEzLjIxNiAzNjUuNTY4bC03Ni44IDEwNS40NzJ6IG0tNDk2LjY0IDBMMS41MzYgNTk2Ljk5MiA0OC42NCA3NDIuNGwyNjIuMTQ0LTg0Ljk5MiA3Ni44LTEwNS40NzJ6IiBmaWxsPSIjRDYzQUZGIiBwLWlkPSIzODc1Ij48L3BhdGg+PHBhdGggZD0iTTMxMC43ODQgNjU3LjQwOEwxNDguOTkyIDg4MC42NGwxMjMuOTA0IDkwLjExMiAxNjIuODE2LTIyMy4yMzJWNjE3LjQ3MnpNNzEzLjIxNiAzNjUuNTY4bDE2MS43OTItMjIzLjIzMi0xMjMuOTA0LTkwLjExMi0xNjEuNzkyIDIyMy4yMzJ2MTMwLjA0OHoiIGZpbGw9IiNGQjAxNUIiIHAtaWQ9IjM4NzYiPjwvcGF0aD48L3N2Zz4=";
    }

    /**
     * 创建应用
     *
     * @param name     {@link String} 名称
     * @param icon     {@link String} 图标
     * @param remark   {@link String} 备注
     * @param groups {@link String} 应用分组
     */
    @Override
    public String create(String name, String icon, String remark, List<String> groups) {
        //1、创建应用
        AppEntity appEntity = createApp(name, icon, remark, groups,
            AuthorizationType.AUTHORIZATION);
        //jwt配置
        AppJwtConfigEntity jwtConfigEntity = new AppJwtConfigEntity();
        jwtConfigEntity.setAppId(appEntity.getId());
        jwtConfigEntity.setBindingType(JwtBindingType.POST);
        //id_token sub 类型
        jwtConfigEntity.setIdTokenSubjectType(JwtIdTokenSubjectType.USER_ID);
        //token有效期（秒）
        jwtConfigEntity.setIdTokenTimeToLive(Duration.ofSeconds(600));
        appJwtConfigRepository.save(jwtConfigEntity);
        // 创建RSA证书
        createCertificate(appEntity.getId(), appEntity.getCode(), AppCertUsingType.JWT_ENCRYPT);
        return String.valueOf(appEntity.getId());
    }

    private final AppJwtConfigConverter appJwtConfigConverter;

    public JwtStandardApplicationServiceImpl(AppJwtConfigRepository appJwtConfigRepository,
                                             AppGroupAssociationRepository appGroupAssociationRepository,
                                             AppJwtConfigConverter appJwtConfigConverter,
                                             AppCertRepository appCertRepository,
                                             AppRepository appRepository,
                                             AppAccountRepository appAccountRepository,
                                             AppAccessPolicyRepository appAccessPolicyRepository) {
        super(appJwtConfigRepository, appGroupAssociationRepository, appCertRepository,
            appRepository, appAccountRepository, appAccessPolicyRepository);
        this.appJwtConfigConverter = appJwtConfigConverter;
    }
}
