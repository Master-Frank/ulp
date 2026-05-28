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

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import cn.frank.ulp.common.entity.app.AppAccountEntity;
import cn.frank.ulp.portal.pojo.request.AppAccountRequest;

/**
 * 应用账户映射
 *
 * @author Frank Zhang
 */
@Mapper(componentModel = "spring")
public interface AppAccountConverter {

    /**
     * 应用账户新增参数转换应用账户实体
     *
     * @param param {@link AppAccountRequest}
     * @return {@link AppAccountEntity}
     */
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "remark", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "updateBy", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "createBy", ignore = true)
    AppAccountEntity appAccountRequestConvertToEntity(AppAccountRequest param);

}
