/*
 * ulp-common - United Login Platform
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
package cn.frank.ulp.common.repository.account;

import java.util.Optional;

import cn.frank.ulp.common.entity.account.po.UserIdpBindPO;
import cn.frank.ulp.support.repository.page.domain.Page;

/**
 * UserIdp Repository Customized
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/12/29 21:27
 */
public interface UserIdpRepositoryCustomized {

    Optional<UserIdpBindPO> selectById(String id);

    /**
     * 根据身份源ID和openId查询
     *
     * @param idpId  {@link  String}
     * @param openId {@link  String}
     * @return {@link Optional}
     */
    Optional<UserIdpBindPO> findByIdpIdAndOpenId(String idpId, String openId);

    /**
     * 根据身份源ID和userId查询
     *
     * @param idpId  {@link  String}
     * @param userId {@link  String}
     * @return {@link Optional}
     */
    Optional<UserIdpBindPO> findByIdpIdAndUserId(String idpId, String userId);

    /**
     * 查询用户身份提供商绑定
     *
     * @param userId {@link  String}
     * @return {@link Page}
     */
    Iterable<UserIdpBindPO> getUserIdpBindList(String userId);
}
