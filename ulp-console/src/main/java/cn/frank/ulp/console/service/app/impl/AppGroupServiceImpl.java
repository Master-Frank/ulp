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
package cn.frank.ulp.console.service.app.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.frank.ulp.audit.context.AuditContext;
import cn.frank.ulp.audit.entity.Target;
import cn.frank.ulp.audit.enums.TargetType;
import cn.frank.ulp.common.entity.app.AppEntity;
import cn.frank.ulp.common.entity.app.AppGroupEntity;
import cn.frank.ulp.common.entity.app.po.AppGroupPO;
import cn.frank.ulp.common.entity.app.query.AppGroupAssociationListQueryParam;
import cn.frank.ulp.common.entity.app.query.AppGroupQueryParam;
import cn.frank.ulp.common.enums.CheckValidityType;
import cn.frank.ulp.common.enums.app.AppGroupType;
import cn.frank.ulp.common.repository.app.AppGroupAssociationRepository;
import cn.frank.ulp.common.repository.app.AppGroupRepository;
import cn.frank.ulp.console.converter.app.AppConverter;
import cn.frank.ulp.console.converter.app.AppGroupConverter;
import cn.frank.ulp.console.pojo.query.app.AppGroupAssociationListQuery;
import cn.frank.ulp.console.pojo.query.app.AppGroupListQuery;
import cn.frank.ulp.console.pojo.result.app.AppGroupGetResult;
import cn.frank.ulp.console.pojo.result.app.AppGroupListResult;
import cn.frank.ulp.console.pojo.result.app.AppListResult;
import cn.frank.ulp.console.pojo.save.app.AppGroupCreateParam;
import cn.frank.ulp.console.pojo.update.app.AppGroupUpdateParam;
import cn.frank.ulp.console.service.app.AppGroupService;
import cn.frank.ulp.support.exception.InfoValidityFailException;
import cn.frank.ulp.support.exception.TopIamException;
import cn.frank.ulp.support.repository.page.domain.Page;
import cn.frank.ulp.support.repository.page.domain.PageModel;
import cn.frank.ulp.support.util.BeanUtils;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import static cn.frank.ulp.support.repository.base.BaseEntity.LAST_MODIFIED_BY;
import static cn.frank.ulp.support.repository.base.BaseEntity.LAST_MODIFIED_TIME;

/**
 * AppGroupServiceImpl
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/8/31 14:23
 */
@Service
@Slf4j
@AllArgsConstructor
public class AppGroupServiceImpl implements AppGroupService {

    /**
     * 获取应用分组（分页）
     *
     * @param pageModel {@link PageModel}
     * @param query     {@link AppGroupListQuery}
     * @return {@link AppGroupListResult}
     */
    @Override
    public Page<AppGroupListResult> getAppGroupList(PageModel pageModel, AppGroupListQuery query) {
        AppGroupQueryParam param = appGroupConverter.appGroupQueryToQueryParam(query);
        //查询映射
        org.springframework.data.domain.Page<AppGroupPO> list = appGroupRepository.getAppGroupList(
            param, PageRequest.of(pageModel.getCurrent(), pageModel.getPageSize()));
        return appGroupConverter.entityConvertToAppGroupListResult(list);
    }

    /**
     * 创建应用分组
     *
     * @param param {@link AppGroupCreateParam}
     * @return {@link Boolean}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean createAppGroup(AppGroupCreateParam param) {
        if (StringUtils.isNotBlank(param.getName())) {
            Boolean validityName = appGroupParamCheck(CheckValidityType.NAME, param.getName(),
                null);
            if (!validityName) {
                throw new InfoValidityFailException("分组名称已存在");
            }
        }
        AppGroupEntity entity = appGroupConverter.appGroupCreateParamConvertToEntity(param);
        entity.setType(AppGroupType.CUSTOM);
        appGroupRepository.save(entity);
        AuditContext.setTarget(Target.builder().id(entity.getId()).name(entity.getName())
            .type(TargetType.APP_GROUP).build());
        return true;
    }

    /**
     * 修改应用分组
     *
     * @param param {@link AppGroupUpdateParam}
     * @return {@link Boolean}
     */
    @Override
    public boolean updateAppGroup(AppGroupUpdateParam param) {
        if (StringUtils.isNotBlank(param.getName())) {
            Boolean validityName = appGroupParamCheck(CheckValidityType.NAME, param.getName(),
                param.getId());
            if (!validityName) {
                throw new InfoValidityFailException("分组名称已存在");
            }
        }
        AppGroupEntity appGroup = appGroupRequireNonNull(param.getId());
        AppGroupEntity entity = appGroupConverter.appGroupUpdateParamConverterToEntity(param);
        BeanUtils.merge(entity, appGroup, LAST_MODIFIED_TIME, LAST_MODIFIED_BY);
        appGroupRepository.save(appGroup);
        AuditContext.setTarget(Target.builder().id(param.getId()).name(param.getName())
            .type(TargetType.APP_GROUP).build());
        return true;
    }

    /**
     * 删除应用分组
     *
     * @param id {@link  String}
     * @return {@link Boolean}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteAppGroup(String id) {
        AppGroupEntity entity = appGroupRequireNonNull(id);
        appGroupRepository.deleteById(id);
        appGroupAssociationRepository.deleteAllByGroupId(id);
        AuditContext.setTarget(
            Target.builder().id(id).name(entity.getName()).type(TargetType.APP_GROUP).build());
        return true;
    }

    /**
     * 获取单个应用分组详情
     *
     * @param id {@link Long}
     * @return {@link AppGroupEntity}
     */
    @Override
    public AppGroupGetResult getAppGroup(String id) {
        Optional<AppGroupEntity> optional = appGroupRepository.findById(id);
        if (optional.isPresent()) {
            AppGroupEntity entity = optional.get();
            return appGroupConverter.entityConvertToAppGroupResult(entity);
        }
        return null;

    }

    /**
     * 查询并检查分组是否为空，非空返回
     *
     * @param id {@link Long}
     * @return {@link AppGroupEntity}
     */
    private AppGroupEntity appGroupRequireNonNull(String id) {
        Optional<AppGroupEntity> optional = appGroupRepository.findById(id);
        if (optional.isEmpty()) {
            AuditContext.setContent("操作失败，应用分组不存在");
            log.warn(AuditContext.getContent());
            throw new TopIamException(AuditContext.getContent());
        }
        return optional.get();
    }

    /**
     * 批量移除应用
     *
     * @param appIds {@link String}
     * @param id     {@link String}
     * @return {@link Boolean}
     */
    @Override
    public Boolean batchRemoveAssociation(String id, List<String> appIds) {
        Optional<AppGroupEntity> optional = appGroupRepository.findById(id);
        //用户组不存在
        if (optional.isEmpty()) {
            AuditContext.setContent("操作失败，应用组不存在");
            log.warn(AuditContext.getContent());
            throw new TopIamException(AuditContext.getContent());
        }
        appIds.forEach(appId -> appGroupAssociationRepository.deleteByGroupIdAndAppId(id, appId));
        List<Target> targets = new ArrayList<>(appIds.stream()
            .map(i -> Target.builder().id(i).name("").type(TargetType.APPLICATION).build())
            .toList());
        targets.add(Target.builder().id(id).name("").type(TargetType.APP_GROUP).build());
        AuditContext.setTarget(targets);
        return true;
    }

    /**
     * 获取应用组内应用列表
     *
     * @param query {@link AppGroupAssociationListQuery}
     * @return {@link AppListResult}
     */
    @Override
    public Page<AppListResult> getAppGroupAssociationList(PageModel model,
                                                          AppGroupAssociationListQuery query) {
        //@formatter:off
        AppGroupAssociationListQueryParam param = appConverter.appGroupAssociationListQueryToQueryParam(query);
        //@formatter:on
        org.springframework.data.domain.Page<AppEntity> page = appGroupAssociationRepository
            .getAppGroupAssociationList(param,
                PageRequest.of(model.getCurrent(), model.getPageSize()));
        return appConverter.entityConvertToAppListResult(page);
    }

    /**
     * 参数有效性验证
     *
     * @param type  {@link CheckValidityType}
     * @param value {@link String}
     * @param id    {@link Long}
     * @return {@link Boolean}
     */
    private Boolean appGroupParamCheck(CheckValidityType type, String value, String id) {
        if (StringUtils.isEmpty(value)) {
            return true;
        }
        //取出前后空格
        value = value.trim();
        AppGroupEntity entity = new AppGroupEntity();
        boolean result = false;
        // ID存在说明是修改操作，查询一下当前数据
        if (Objects.nonNull(id)) {
            entity = appGroupRepository.findById(id).orElse(new AppGroupEntity());
        }
        //分组名称
        if (CheckValidityType.NAME.equals(type)) {
            if (StringUtils.equals(entity.getName(), value)) {
                return true;
            }
            result = !appGroupRepository.exists(Example.of(new AppGroupEntity().setName(value)));
        }
        return result;
    }

    /**
     * AppGroupRepository
     */
    private final AppGroupRepository            appGroupRepository;

    /**
     * AppGroupConverter
     */
    private final AppGroupConverter             appGroupConverter;

    private final AppConverter                  appConverter;

    /**
     * AppGroupAssociationRepository
     */
    private final AppGroupAssociationRepository appGroupAssociationRepository;
}
