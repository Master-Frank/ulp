/*
 * ulp-console - United Login Platform
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
package cn.frank.ulp.console.converter.identitysource;

import java.util.*;

import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn.frank.ulp.common.entity.identitysource.IdentitySourceEntity;
import cn.frank.ulp.common.enums.identitysource.IdentitySourceProvider;
import cn.frank.ulp.common.exception.IdentitySourceProviderNotSupportedException;
import cn.frank.ulp.common.jackjson.encrypt.EncryptionModule;
import cn.frank.ulp.console.pojo.query.identity.IdentitySourceListQuery;
import cn.frank.ulp.console.pojo.result.identitysource.IdentitySourceConfigGetResult;
import cn.frank.ulp.console.pojo.result.identitysource.IdentitySourceGetResult;
import cn.frank.ulp.console.pojo.result.identitysource.IdentitySourceListResult;
import cn.frank.ulp.console.pojo.save.identitysource.IdentitySourceConfigSaveParam;
import cn.frank.ulp.console.pojo.save.identitysource.IdentitySourceCreateParam;
import cn.frank.ulp.console.pojo.update.identity.IdentitySourceUpdateParam;
import cn.frank.ulp.core.context.ContextService;
import cn.frank.ulp.identitysource.core.IdentitySourceConfig;
import cn.frank.ulp.identitysource.dingtalk.DingTalkConfig;
import cn.frank.ulp.identitysource.feishu.FeiShuConfig;
import cn.frank.ulp.support.exception.UlpException;
import cn.frank.ulp.support.util.BeanUtils;
import cn.frank.ulp.support.validation.ValidationUtils;

import jakarta.persistence.criteria.Predicate;
import jakarta.validation.ConstraintViolationException;
import static cn.frank.ulp.common.constant.SynchronizerConstants.EVENT_RECEIVE_PATH;
import static cn.frank.ulp.common.entity.identitysource.IdentitySourceEntity.NAME_FIELD_NAME;
import static cn.frank.ulp.support.constant.EiamConstants.PATH_SEPARATOR;
import static cn.frank.ulp.support.repository.base.BaseEntity.LAST_MODIFIED_TIME;

/**
 * 身份源转换器
 *
 * @author Frank Zhang
 */
@Mapper(componentModel = "spring")
public interface IdentitySourceConverter {

    /**
     * 回调地址名称
     */
    String CALLBACK_URL = "callbackUrl";

    /**
     * 身份源平台枚举转换器到身份源平台结果
     *
     * @param values {@link IdentitySourceEntity}
     * @return {@link List}
     */
    default cn.frank.ulp.support.repository.page.domain.Page<IdentitySourceListResult> entityConverterToIdentitySourceListResult(Page<IdentitySourceEntity> values) {

        cn.frank.ulp.support.repository.page.domain.Page<IdentitySourceListResult> result = new cn.frank.ulp.support.repository.page.domain.Page<>();
        if (!CollectionUtils.isEmpty(values.getContent())) {
            ArrayList<IdentitySourceListResult> list = new ArrayList<>();
            for (IdentitySourceEntity entity : values.getContent()) {
                list.add(entityConverterToIdentitySourceListResult(entity));
            }

            //@formatter:off
            result.setPagination(cn.frank.ulp.support.repository.page.domain.Page.Pagination.builder()
                    .total(values.getTotalElements())
                    .totalPages(values.getTotalPages())
                    .current(values.getPageable().getPageNumber() + 1)
                    .build());
            //@formatter:on
            result.setList(list);
        }
        return result;
    }

    /**
     * 身份源平台枚举转换器到身份源平台结果
     *
     * @param value {@link IdentitySourceEntity}
     * @return {@link List}
     */
    @Mapping(target = "desc", source = "provider.desc")
    @Mapping(target = "provider", source = "provider.code")
    @Mapping(target = "icon", ignore = true)
    IdentitySourceListResult entityConverterToIdentitySourceListResult(IdentitySourceEntity value);

    /**
     * 创建入参转换为实体类
     *
     * @param param {@link IdentitySourceCreateParam}
     * @return {@link IdentitySourceEntity}
     */

    @Mapping(target = "basicConfig", ignore = true)
    @Mapping(target = "code", expression = "java(org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric(32).toLowerCase())")
    @Mapping(target = "configured", expression = "java(Boolean.FALSE)")
    @Mapping(target = "enabled", expression = "java(Boolean.TRUE)")
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "jobConfig", ignore = true)
    @Mapping(target = "strategyConfig", ignore = true)
    @Mapping(target = "updateBy", ignore = true)
    @Mapping(target = "remark", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "createBy", ignore = true)
    IdentitySourceEntity createParamConverterToEntity(IdentitySourceCreateParam param);

    /**
     * entity转换为详细信息结果
     *
     * @param entity {@link IdentitySourceEntity}
     * @return {@link IdentitySourceGetResult}
     */
    IdentitySourceGetResult entityConverterToIdentitySourceGetResult(IdentitySourceEntity entity);

    /**
     * 保存入参转换为实体类
     *
     * @param param {@link IdentitySourceCreateParam}
     * @return {@link IdentitySourceEntity}
     */

    @Mapping(target = "code", ignore = true)
    @Mapping(target = "configured", ignore = true)
    @Mapping(target = "strategyConfig", ignore = true)
    @Mapping(target = "basicConfig", ignore = true)
    @Mapping(target = "jobConfig", ignore = true)
    @Mapping(target = "provider", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "updateBy", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "createBy", ignore = true)
    IdentitySourceEntity updateParamConverterToEntity(IdentitySourceUpdateParam param);

    /**
     * 保存配置参数转entity
     *
     * @param param {@link  IdentitySourceConfigSaveParam}
     * @return {@link  IdentitySourceEntity}
     */

    @Mapping(target = "code", ignore = true)
    @Mapping(target = "configured", expression = "java(Boolean.TRUE)")
    @Mapping(target = "basicConfig", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "updateBy", ignore = true)
    @Mapping(target = "remark", ignore = true)
    @Mapping(target = "provider", ignore = true)
    @Mapping(target = "name", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "createBy", ignore = true)
    IdentitySourceEntity saveConfigParamConverterToEntity(IdentitySourceConfigSaveParam param);

    /**
     * 保存配置参数转entity
     *
     * @param param    {@link  IdentitySourceConfigSaveParam}
     * @param provider {@link  IdentitySourceProvider}
     * @return {@link  IdentitySourceEntity}
     */
    default IdentitySourceEntity saveConfigParamConverterToEntity(IdentitySourceConfigSaveParam param,
                                                                  IdentitySourceProvider provider) {
        try {
            ValidationUtils.ValidationResult<?> validationResult;
            IdentitySourceConfig clientConfig;
            ObjectMapper objectMapper = EncryptionModule.serializerEncrypt();
            switch (provider) {
                case DINGTALK -> clientConfig = objectMapper
                    .readValue(param.getBasicConfig().toJSONString(), DingTalkConfig.class);
                case FEISHU -> clientConfig = objectMapper
                    .readValue(param.getBasicConfig().toJSONString(), FeiShuConfig.class);
                default -> throw new IdentitySourceProviderNotSupportedException();
            }
            //放置参数，并验证参数
            if (!Objects.nonNull(clientConfig)) {
                throw new NullPointerException("提供商配置不能为空!");
            }
            validationResult = ValidationUtils.validateEntity(clientConfig);
            //处理异常
            if (validationResult.isHasErrors()) {
                throw new ConstraintViolationException(validationResult.getConstraintViolations());
            }
            //封装数据
            IdentitySourceEntity source = saveConfigParamConverterToEntity(param);
            String value = objectMapper
                .activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(),
                    ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY)
                .writeValueAsString(clientConfig);
            source.setBasicConfig(value);
            return source;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 查询身份源列表参数转 Request
     *
     * @param listQuery     {@link  IdentitySourceListQuery}
     * @return {@link Specification }
     */
    default Specification<IdentitySourceEntity> queryIdentitySourceListParamConvertToPredicate(IdentitySourceListQuery listQuery) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.isNotBlank(listQuery.getName())) {
                predicates.add(cb.like(root.get(NAME_FIELD_NAME), "%" + listQuery.getName() + "%"));
            }
            query.where(predicates.toArray(new Predicate[0]));
            query.orderBy(cb.desc(root.get(LAST_MODIFIED_TIME)));
            return query.getRestriction();
        };
    }

    /**
     * entity转换为配置结果
     *
     * @param entity {@link IdentitySourceEntity}
     * @return {@link IdentitySourceConfigGetResult}
     */
    default IdentitySourceConfigGetResult entityConverterToIdentitySourceConfigGetResult(IdentitySourceEntity entity) {
        if (entity == null) {
            return null;
        }
        IdentitySourceConfigGetResult identitySourceResult = new IdentitySourceConfigGetResult();
        if (entity.getId() != null) {
            identitySourceResult.setId(String.valueOf(entity.getId()));
        }
        identitySourceResult.setConfigured(entity.getConfigured());
        identitySourceResult.setJobConfig(entity.getJobConfig());
        identitySourceResult.setStrategyConfig(entity.getStrategyConfig());
        try {
            ObjectMapper objectMapper = EncryptionModule.deserializerDecrypt();
            Map<String, Object> value = new HashMap<>(16);
            value.put(CALLBACK_URL, ContextService.getConsolePublicBaseUrl() + EVENT_RECEIVE_PATH
                                    + PATH_SEPARATOR + entity.getCode());
            IdentitySourceConfig config;
            // 指定序列化输入的类型
            objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(),
                ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
            if (!Objects.isNull(entity.getBasicConfig())) {
                switch (entity.getProvider()) {
                    case DINGTALK -> config = objectMapper.readValue(entity.getBasicConfig(),
                        DingTalkConfig.class);
                    case FEISHU -> config = objectMapper.readValue(entity.getBasicConfig(),
                        FeiShuConfig.class);
                    default -> throw new IdentitySourceProviderNotSupportedException();
                }
                value.putAll(BeanUtils.beanToMap(config));
            }
            identitySourceResult.setBasicConfig(value);
        } catch (JsonProcessingException e) {
            throw new UlpException(e.getMessage());
        }
        return identitySourceResult;
    }
}
