/*
 * ulp-portal - United Login Platform
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
package cn.frank.ulp.portal.converter;

import java.util.List;

import org.mapstruct.Mapper;

import com.google.common.collect.Lists;

import cn.frank.ulp.common.entity.app.po.AppGroupPO;
import cn.frank.ulp.common.entity.app.query.AppGroupQueryParam;
import cn.frank.ulp.portal.pojo.query.GetAppGroupListQuery;
import cn.frank.ulp.portal.pojo.result.AppGroupListResult;

/**
 * 分组映射
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/8/31 15:45
 */
@Mapper(componentModel = "spring")
public interface AppGroupConverter {

    /**
     * 实体转换为分组列表结果
     *
     * @param appGroupPoList {@link List}
     * @return {@link List}
     */
    default List<AppGroupListResult> entityConvertToAppGroupListResult(List<AppGroupPO> appGroupPoList) {
        List<AppGroupListResult> list = Lists.newArrayList();
        for (AppGroupPO po : appGroupPoList) {
            AppGroupListResult result = entityConvertToAppGroupListResult(po);
            list.add(result);
        }
        return list;
    }

    /**
     * 实体转分组管理列表
     *
     * @param appGroupPo {@link AppGroupPO}
     * @return {@link AppGroupListResult}
     */
    AppGroupListResult entityConvertToAppGroupListResult(AppGroupPO appGroupPo);

    /**
     * 查询参数转换
     *
     * @param query {@link GetAppGroupListQuery}
     * @return {@link AppGroupQueryParam}
     */
    AppGroupQueryParam appGroupQueryToQueryParam(GetAppGroupListQuery query);
}
