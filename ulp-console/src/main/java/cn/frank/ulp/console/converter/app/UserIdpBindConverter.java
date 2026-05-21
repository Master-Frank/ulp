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

import org.mapstruct.Mapper;

import cn.frank.ulp.common.entity.account.po.UserIdpBindPO;
import cn.frank.ulp.console.pojo.result.app.UserIdpBindListResult;
import cn.frank.ulp.support.repository.page.domain.Page;

/**
 * 用户身份提供商绑定
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/11/3 21:08
 */
@Mapper(componentModel = "spring")
public interface UserIdpBindConverter {

    /**
     * 用户身份提供商绑定关系分页结果
     *
     * @param page {@link Page}
     * @return {@link Page}
     */
    default List<UserIdpBindListResult> userIdpBindEntityConvertToUserIdpBindListResult(Iterable<UserIdpBindPO> page) {
        List<UserIdpBindListResult> list = new ArrayList<>();
        for (UserIdpBindPO entity : page) {
            list.add(entityConvertToAppAccountResult(entity));
        }
        return list;
    }

    /**
     * 用户身份提供商绑定关系转换结果
     *
     * @param userIdpBindPo {@link UserIdpBindPO}
     * @return {@link UserIdpBindListResult}
     */
    UserIdpBindListResult entityConvertToAppAccountResult(UserIdpBindPO userIdpBindPo);
}
