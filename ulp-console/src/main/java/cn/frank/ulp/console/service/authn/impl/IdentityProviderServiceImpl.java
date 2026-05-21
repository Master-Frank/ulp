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
package cn.frank.ulp.console.service.authn.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.frank.ulp.audit.context.AuditContext;
import cn.frank.ulp.audit.entity.Target;
import cn.frank.ulp.audit.enums.TargetType;
import cn.frank.ulp.common.entity.authn.IdentityProviderEntity;
import cn.frank.ulp.common.repository.authentication.IdentityProviderRepository;
import cn.frank.ulp.console.converter.authn.IdentityProviderConverter;
import cn.frank.ulp.console.pojo.query.authn.IdentityProviderListQuery;
import cn.frank.ulp.console.pojo.result.authn.IdentityProviderCreateResult;
import cn.frank.ulp.console.pojo.result.authn.IdentityProviderListResult;
import cn.frank.ulp.console.pojo.result.authn.IdentityProviderResult;
import cn.frank.ulp.console.pojo.save.authn.IdentityProviderCreateParam;
import cn.frank.ulp.console.pojo.update.authn.IdpUpdateParam;
import cn.frank.ulp.console.service.authn.IdentityProviderService;
import cn.frank.ulp.support.context.ApplicationContextService;
import cn.frank.ulp.support.exception.TopIamException;
import cn.frank.ulp.support.repository.page.domain.Page;
import cn.frank.ulp.support.repository.page.domain.PageModel;
import cn.frank.ulp.support.util.BeanUtils;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import static cn.frank.ulp.common.constant.ConfigBeanNameConstants.DEFAULT_SECURITY_FILTER_CHAIN;

/**
 * <p>
 * 认证源配置 服务实现类
 * </p>
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020-08-16
 */
@Slf4j
@Service
@AllArgsConstructor
public class IdentityProviderServiceImpl implements IdentityProviderService {

    /**
     * 平台是否启用
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    @Override
    public Boolean identityProviderIsEnable(String id) {
        Optional<IdentityProviderEntity> optional = identityProviderRepository.findById(id);
        return optional.isPresent() && optional.get().getEnabled();
    }

    /**
     * 通过平台类型获取
     *
     * @param provider {@link String}
     * @return {@link IdentityProviderEntity}
     */
    @Override
    public List<IdentityProviderEntity> getByIdentityProvider(String provider) {
        return identityProviderRepository.findByType(provider);
    }

    /**
     * 认证源列表
     *
     * @param pageModel {@link PageModel }
     * @param query     {@link  IdentityProviderListQuery}
     * @return {@link List}
     */
    @Override
    public Page<IdentityProviderListResult> getIdentityProviderList(PageModel pageModel,
                                                                    IdentityProviderListQuery query) {
        Specification<IdentityProviderEntity> specification = identityProviderConverter
            .queryIdentityProviderListParamConvertToSpecification(query);
        PageRequest request = PageRequest.of(pageModel.getCurrent(), pageModel.getPageSize());
        org.springframework.data.domain.Page<IdentityProviderEntity> list = identityProviderRepository
            .findAll(specification, request);
        return identityProviderConverter.entityConverterToIdentityProviderResult(list);
    }

    /**
     * 认证源详情
     *
     * @param id {@link String}
     * @return {@link IdentityProviderResult}
     */
    @Override
    public IdentityProviderResult getIdentityProvider(String id) {
        Optional<IdentityProviderEntity> optional = identityProviderRepository.findById(id);
        if (optional.isPresent()) {
            return identityProviderConverter
                .entityConverterToIdentityProviderDetailResult(optional.get());
        }
        return new IdentityProviderResult();
    }

    /**
     * 创建认证源
     *
     * @param param {@link IdentityProviderCreateParam}
     * @return {@link IdentityProviderCreateResult}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public IdentityProviderCreateResult createIdp(IdentityProviderCreateParam param) {
        //转换对象
        IdentityProviderEntity data = identityProviderConverter
            .identityProviderCreateParamConverterToEntity(param);
        identityProviderRepository.save(data);
        ApplicationContextService.refresh(DEFAULT_SECURITY_FILTER_CHAIN);
        AuditContext.setTarget(Target.builder().id(data.getId()).name(data.getName())
            .type(TargetType.IDENTITY_PROVIDER).build());
        return IdentityProviderCreateResult.builder().id(String.valueOf(data.getId()))
            .type(data.getType()).build();
    }

    /**
     * 更新身份验证源
     *
     * @param param {@link IdpUpdateParam}
     * @return {@link Boolean}
     */
    @Override
    public Boolean updateIdentityProvider(IdpUpdateParam param) {
        Optional<IdentityProviderEntity> optional = identityProviderRepository
            .findById(param.getId());
        if (optional.isPresent()) {
            IdentityProviderEntity entity = optional.get();
            //转换对象
            IdentityProviderEntity data = identityProviderConverter
                .identityProviderUpdateParamConverterToEntity(param);
            BeanUtils.merge(data, entity);
            identityProviderRepository.save(entity);
            ApplicationContextService.refresh(DEFAULT_SECURITY_FILTER_CHAIN);
            AuditContext.setTarget(Target.builder().id(entity.getId()).name(entity.getName())
                .type(TargetType.IDENTITY_PROVIDER).typeName(TargetType.IDENTITY_PROVIDER.getDesc())
                .build());
            return true;
        }
        throw new NullPointerException("系统不存在该身份源");
    }

    /**
     * 删除认证源
     *
     * @param id {@link  String}
     * @return {@link Boolean}
     */
    @Override
    public Boolean deleteIdentityProvider(String id) {
        Optional<IdentityProviderEntity> optional = identityProviderRepository.findById(id);
        //管理员不存在
        if (optional.isEmpty()) {
            AuditContext.setContent("删除失败，认证源不存在");
            log.warn(AuditContext.getContent());
            throw new TopIamException(AuditContext.getContent());
        }
        identityProviderRepository.deleteById(id);
        ApplicationContextService.refresh(DEFAULT_SECURITY_FILTER_CHAIN);
        AuditContext.setTarget(Target.builder().id(id).name(optional.get().getName())
            .type(TargetType.IDENTITY_PROVIDER).build());
        return Boolean.TRUE;
    }

    /**
     * 更改认证源状态
     *
     * @param id      {@link String}
     * @param enabled {@link Boolean}
     * @return {@link Boolean}
     */
    @Override
    public Boolean updateIdentityProviderStatus(String id, Boolean enabled) {
        Optional<IdentityProviderEntity> optional = identityProviderRepository.findById(id);
        //管理员不存在
        if (optional.isEmpty()) {
            AuditContext.setContent("删除失败，认证源不存在");
            log.warn(AuditContext.getContent());
            throw new TopIamException(AuditContext.getContent());
        }
        boolean result = identityProviderRepository.updateIdentityProviderStatus(id, enabled) > 0;
        ApplicationContextService.refresh(DEFAULT_SECURITY_FILTER_CHAIN);
        AuditContext.setTarget(Target.builder().id(id).name(optional.get().getName())
            .type(TargetType.IDENTITY_PROVIDER).build());
        return result;
    }

    /**
     * 身份源身份转换器
     */
    private final IdentityProviderConverter  identityProviderConverter;
    /**
     * 身份源repository
     */
    private final IdentityProviderRepository identityProviderRepository;
}
