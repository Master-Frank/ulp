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

import java.util.ArrayList;
import java.util.List;

import org.mapstruct.Mapper;

import cn.frank.ulp.authentication.common.IdentityProviderType;
import cn.frank.ulp.common.entity.authn.IdentityProviderEntity;
import cn.frank.ulp.portal.pojo.result.IdentityProviderResult;

/**
 * AuthenticationConverter
 *
 * @author Frank Zhang
 */
@Mapper(componentModel = "spring")
public interface LoginConfigConverter {

    /**
     * 实体转身份提供商列表
     *
     * @param list {@link List}
     * @return {@link List}
     */
    default List<IdentityProviderResult> entityConverterToLoginConfigListResult(List<IdentityProviderEntity> list) {
        List<IdentityProviderResult> result = new ArrayList<>();
        for (IdentityProviderEntity entity : list) {
            IdentityProviderResult idp = new IdentityProviderResult();
            idp.setCode(entity.getCode());
            idp.setName(entity.getName());
            idp.setType(entity.getType());
            idp.setCategory(entity.getCategory());
            idp.setAuthorizationUri(IdentityProviderType.getIdentityProviderType(entity.getType())
                .getAuthorizationPathPrefix() + "/" + entity.getCode());
            result.add(idp);
        }
        return result;
    }
}
