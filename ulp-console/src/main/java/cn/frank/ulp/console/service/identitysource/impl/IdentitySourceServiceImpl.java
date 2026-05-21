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
package cn.frank.ulp.console.service.identitysource.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import cn.frank.ulp.audit.context.AuditContext;
import cn.frank.ulp.audit.entity.Target;
import cn.frank.ulp.audit.enums.TargetType;
import cn.frank.ulp.common.constant.AccountConstants;
import cn.frank.ulp.common.entity.identitysource.IdentitySourceEntity;
import cn.frank.ulp.common.repository.identitysource.IdentitySourceRepository;
import cn.frank.ulp.console.converter.identitysource.IdentitySourceConverter;
import cn.frank.ulp.console.pojo.other.IdentitySourceConfigValidatorParam;
import cn.frank.ulp.console.pojo.query.identity.IdentitySourceListQuery;
import cn.frank.ulp.console.pojo.result.identitysource.IdentitySourceListResult;
import cn.frank.ulp.console.pojo.save.authn.IdentityProviderCreateParam;
import cn.frank.ulp.console.pojo.save.identitysource.IdentitySourceConfigSaveParam;
import cn.frank.ulp.console.pojo.save.identitysource.IdentitySourceCreateParam;
import cn.frank.ulp.console.pojo.save.identitysource.IdentitySourceCreateResult;
import cn.frank.ulp.console.pojo.update.identity.IdentitySourceUpdateParam;
import cn.frank.ulp.console.service.identitysource.IdentitySourceService;
import cn.frank.ulp.identitysource.dingtalk.DingTalkConfig;
import cn.frank.ulp.identitysource.dingtalk.DingTalkConfigValidator;
import cn.frank.ulp.identitysource.feishu.FeiShuConfig;
import cn.frank.ulp.identitysource.feishu.FeiShuConfigValidator;
import cn.frank.ulp.support.exception.TopIamException;
import cn.frank.ulp.support.repository.page.domain.Page;
import cn.frank.ulp.support.repository.page.domain.PageModel;
import cn.frank.ulp.support.util.BeanUtils;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import static cn.frank.ulp.support.repository.base.BaseEntity.LAST_MODIFIED_BY;
import static cn.frank.ulp.support.repository.base.BaseEntity.LAST_MODIFIED_TIME;

/**
 * <p>
 * 身份源配置 服务类
 * </p>
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020-08-16
 */
@Slf4j
@Service
@AllArgsConstructor
@CacheConfig(cacheNames = { AccountConstants.IDS_CACHE_NAME })
public class IdentitySourceServiceImpl implements IdentitySourceService {

    /**
     * 身份源列表
     *
     * @param pageModel {@link  PageModel}
     * @return {@link List}
     */
    @Override
    public Page<IdentitySourceListResult> getIdentitySourceList(IdentitySourceListQuery query,
                                                                PageModel pageModel) {
        Specification<IdentitySourceEntity> specification = identitySourceConverter
            .queryIdentitySourceListParamConvertToPredicate(query);
        org.springframework.data.domain.Page<IdentitySourceEntity> list = identitySourceRepository
            .findAll(specification,
                PageRequest.of(pageModel.getCurrent(), pageModel.getPageSize()));
        return identitySourceConverter.entityConverterToIdentitySourceListResult(list);
    }

    /**
     * 身份源详情
     *
     * @param id {@link String}
     * @return {@link IdentitySourceEntity}
     */
    @Override
    public IdentitySourceEntity getIdentitySource(String id) {
        Optional<IdentitySourceEntity> provider = identitySourceRepository.findById(id);
        return provider.orElse(null);
    }

    /**
     * 保存身份源
     *
     * @param param {@link IdentityProviderCreateParam}
     * @return {@link IdentitySourceCreateResult}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public IdentitySourceCreateResult createIdentitySource(IdentitySourceCreateParam param) {
        IdentitySourceEntity entity = identitySourceConverter.createParamConverterToEntity(param);
        identitySourceRepository.save(entity);
        AuditContext.setTarget(Target.builder().id(entity.getId()).name(entity.getName())
            .type(TargetType.IDENTITY_SOURCE).build());
        return new IdentitySourceCreateResult(entity.getId());
    }

    /**
     * 修改身份源
     *
     * @param param {@link IdentitySourceUpdateParam}
     * @return {@link Boolean}
     */
    @Override
    public Boolean updateIdentitySource(IdentitySourceUpdateParam param) {
        IdentitySourceEntity source = identitySourceConverter.updateParamConverterToEntity(param);
        IdentitySourceEntity entity = getIdentitySource(param.getId());
        BeanUtils.merge(source, entity, LAST_MODIFIED_TIME, LAST_MODIFIED_BY);
        identitySourceRepository.save(entity);
        AuditContext.setTarget(Target.builder().id(entity.getId()).name(entity.getName())
            .type(TargetType.IDENTITY_SOURCE).build());
        return true;
    }

    /**
     * 禁用身份源
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    @Override
    public Boolean disableIdentitySource(String id) {
        Optional<IdentitySourceEntity> optional = identitySourceRepository.findById(id);
        //身份源不存在
        if (optional.isEmpty()) {
            AuditContext.setContent("操作失败，身份源不存在");
            log.warn(AuditContext.getContent());
            throw new TopIamException(AuditContext.getContent());
        }
        Integer count = identitySourceRepository.updateIdentitySourceStatus(id, Boolean.FALSE);
        AuditContext.setTarget(Target.builder().id(id).name(optional.get().getName())
            .type(TargetType.IDENTITY_SOURCE).build());
        return count > 0;
    }

    /**
     * 启用身份源
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    @Override
    public Boolean enableIdentitySource(String id) {
        Optional<IdentitySourceEntity> optional = identitySourceRepository.findById(id);
        //用户不存在
        if (optional.isEmpty()) {
            AuditContext.setContent("操作失败，身份源不存在");
            log.warn(AuditContext.getContent());
            throw new TopIamException(AuditContext.getContent());
        }
        Integer count = identitySourceRepository.updateIdentitySourceStatus(id, Boolean.TRUE);
        AuditContext.setTarget(Target.builder().id(id).name(optional.get().getName())
            .type(TargetType.IDENTITY_SOURCE).build());
        return count > 0;
    }

    /**
     * 删除身份源
     *
     * @param id {@link  String}
     * @return {@link  Boolean}
     */
    @Override
    public Boolean deleteIdentitySource(String id) {
        Optional<IdentitySourceEntity> optional = identitySourceRepository.findById(id);
        //用户不存在
        if (optional.isEmpty()) {
            AuditContext.setContent("操作失败，身份源不存在");
            log.warn(AuditContext.getContent());
            throw new TopIamException(AuditContext.getContent());
        }
        identitySourceRepository.deleteById(id);
        AuditContext.setTarget(Target.builder().id(id).name(optional.get().getName())
            .type(TargetType.IDENTITY_SOURCE).build());
        return true;
    }

    /**
     * 保存身份源配置
     *
     * @param param {@link  IdentitySourceConfigSaveParam}
     * @return {@link  Boolean}
     */
    @Override
    public Boolean saveIdentitySourceConfig(IdentitySourceConfigSaveParam param) {
        IdentitySourceEntity entity = getIdentitySource(param.getId());
        //转换
        IdentitySourceEntity source = identitySourceConverter
            .saveConfigParamConverterToEntity(param, entity.getProvider());
        //合并对象
        BeanUtils.merge(source, entity, LAST_MODIFIED_BY, LAST_MODIFIED_TIME);
        identitySourceRepository.save(entity);
        AuditContext.setTarget(Target.builder().id(entity.getId()).name(entity.getName())
            .type(TargetType.IDENTITY_SOURCE).build());
        return true;
    }

    /**
     * 更新身份源策略
     *
     * @param id             {@link Long} 主键
     * @param strategyConfig {@link String} 策略
     */
    @Override
    public void updateStrategyConfig(String id, String strategyConfig) {
        Optional<IdentitySourceEntity> optional = identitySourceRepository.findById(id);
        //用户不存在
        if (optional.isEmpty()) {
            AuditContext.setContent("操作失败，身份源不存在");
            log.warn(AuditContext.getContent());
            throw new TopIamException(AuditContext.getContent());
        }
        identitySourceRepository.updateStrategyConfig(id, strategyConfig);
        AuditContext.setTarget(Target.builder().id(id).name(optional.get().getName())
            .type(TargetType.IDENTITY_SOURCE).build());

    }

    /**
     * 身份源配置验证
     *
     * @param param {@link  IdentitySourceConfigValidatorParam}
     * @return {@link  Boolean}
     */
    @Override
    public Boolean identitySourceConfigValidator(IdentitySourceConfigValidatorParam param) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        try {
            return switch (param.getProvider()) {
                //钉钉
                case DINGTALK -> {
                    DingTalkConfig config = objectMapper
                            .readValue(param.getConfig().toJSONString(), DingTalkConfig.class);;
                    yield new DingTalkConfigValidator().validate(config);
                }
                case FEISHU -> {
                    FeiShuConfig config = objectMapper
                            .readValue(param.getConfig().toJSONString(), FeiShuConfig.class);
                    yield new FeiShuConfigValidator().validate(config);
                }
                default -> throw new TopIamException("暂未支持此提供商连接验证");
            };
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 身份验证源转换器
     */
    private final IdentitySourceConverter  identitySourceConverter;

    /**
     * 身份源
     */
    private final IdentitySourceRepository identitySourceRepository;

}
