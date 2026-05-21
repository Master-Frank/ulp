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
package cn.frank.ulp.console.converter.app;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.springframework.util.CollectionUtils;

import cn.frank.ulp.application.ApplicationService;
import cn.frank.ulp.application.ApplicationServiceLoader;
import cn.frank.ulp.common.entity.app.AppAccessPolicyEntity;
import cn.frank.ulp.common.entity.app.po.AppAccessPolicyPO;
import cn.frank.ulp.common.entity.app.query.AppAccessPolicyQueryParam;
import cn.frank.ulp.console.pojo.query.app.AppAccessPolicyQuery;
import cn.frank.ulp.console.pojo.result.app.AppAccessPolicyResult;
import cn.frank.ulp.console.pojo.save.app.AppAccessPolicyCreateParam;
import cn.frank.ulp.support.context.ApplicationContextService;
import cn.frank.ulp.support.repository.page.domain.Page;

/**
 * 应用授权策略 Converter
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/6/4 21:57
 */
@Mapper(componentModel = "spring")
public interface AppAccessPolicyConverter {

    /**
     * 应用授权策略分页列表转换为应用授权策略分页结果
     *
     * @param page {@link AppAccessPolicyEntity}
     * @return {@link AppAccessPolicyResult}
     */
    default Page<AppAccessPolicyResult> appPolicyEntityListConvertToAppPolicyResult(org.springframework.data.domain.Page<AppAccessPolicyPO> page) {
        Page<AppAccessPolicyResult> result = new Page<>();
        if (!CollectionUtils.isEmpty(page.getContent())) {
            List<AppAccessPolicyResult> list = new ArrayList<>();
            for (AppAccessPolicyPO po : page.getContent()) {
                AppAccessPolicyResult policy = entityConvertToAppPolicyResult(po);
                //图标未配置，所以先从模版中拿
                if (StringUtils.isBlank(policy.getAppIcon())) {
                    ApplicationService applicationService = getApplicationServiceLoader()
                        .getApplicationService(policy.getAppTemplate());
                    policy.setAppIcon(applicationService.getBase64Icon());
                }
                list.add(policy);
            }
            //@formatter:off
            result.setPagination(Page.Pagination.builder()
                    .total(page.getTotalElements())
                    .totalPages(page.getTotalPages())
                    .current(page.getPageable().getPageNumber() + 1)
                    .build());
            //@formatter:on
            result.setList(list);
        }
        return result;
    }

    /**
     * 应用授权策略实体转换为应用授权策略结果
     *
     * @param entity {@link AppAccessPolicyEntity}
     * @return {@link AppAccessPolicyEntity}
     */
    AppAccessPolicyResult entityConvertToAppPolicyResult(AppAccessPolicyPO entity);

    /**
     * 应用授权策略添加参数转换为应用授权策略实体
     *
     * @param param {@link  AppAccessPolicyCreateParam}
     * @return {@link AppAccessPolicyEntity}
     */
    default List<AppAccessPolicyEntity> appPolicyCreateParamConvertToEntity(AppAccessPolicyCreateParam param) {
        if (param == null) {
            return new ArrayList<>();
        }
        List<AppAccessPolicyEntity> list = new ArrayList<>();
        for (String subjectId : param.getSubjectIds()) {
            AppAccessPolicyEntity entity = new AppAccessPolicyEntity();
            entity.setAppId(param.getAppId());
            entity.setSubjectType(param.getSubjectType());
            entity.setEnabled(true);
            entity.setSubjectId(subjectId);
            list.add(entity);
        }
        return list;
    }

    /**
     * 获取 ApplicationServiceLoader
     *
     * @return {@link ApplicationServiceLoader}
     */
    private ApplicationServiceLoader getApplicationServiceLoader() {
        return ApplicationContextService.getBean(ApplicationServiceLoader.class);
    }

    /**
     * 转查询参数
     *
     * @param query {@link AppAccessPolicyQuery}
     * @return {@link AppAccessPolicyQueryParam}
     */
    AppAccessPolicyQueryParam appAccessPolicyQueryToQueryParam(AppAccessPolicyQuery query);

}
