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
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

import cn.frank.ulp.common.entity.app.AppCertEntity;
import cn.frank.ulp.console.pojo.query.app.AppCertQuery;
import cn.frank.ulp.console.pojo.result.app.AppCertListResult;
import static cn.frank.ulp.common.entity.app.AppCertEntity.APP_ID_FIELD_NAME;
import static cn.frank.ulp.common.entity.app.AppCertEntity.USING_TYPE_FIELD_NAME;

/**
 * 应用证书Converter
 *
 * @author Frank Zhang
 */
@Mapper(componentModel = "spring")
public interface AppCertConverter {
    /**
     * 查询应用列表参数转换为  Example
     *
     * @param query {@link AppCertQuery} query
     * @return {@link Example}
     */
    default Example<AppCertEntity> queryAppCertListParamConvertToExample(AppCertQuery query) {
        //查询条件
        AppCertEntity entity = new AppCertEntity();
        ExampleMatcher exampleMatcher = ExampleMatcher.matching();
        if (!StringUtils.isBlank(query.getAppId())) {
            exampleMatcher.withMatcher(APP_ID_FIELD_NAME,
                ExampleMatcher.GenericPropertyMatchers.exact());
            entity.setAppId(query.getAppId());

        }
        if (Objects.nonNull(query.getUsingType())) {
            exampleMatcher.withMatcher(USING_TYPE_FIELD_NAME,
                ExampleMatcher.GenericPropertyMatchers.exact());
            entity.setUsingType(query.getUsingType());
        }
        return Example.of(entity, exampleMatcher);
    }

    /**
     * 实体转换为应用程序证书列表结果
     *
     * @param list {@link List}
     * @return {@link List}
     */
    default List<AppCertListResult> entityConvertToAppCertListResult(List<AppCertEntity> list) {
        List<AppCertListResult> results = new ArrayList<>();
        for (AppCertEntity cert : list) {
            results.add(entityConvertToAppCertListResult(cert));
        }
        return results;
    }

    /**
     * 实体转换为应用程序证书列表结果
     *
     * @param list {@link List}
     * @return {@link List}
     */
    AppCertListResult entityConvertToAppCertListResult(AppCertEntity list);
}
