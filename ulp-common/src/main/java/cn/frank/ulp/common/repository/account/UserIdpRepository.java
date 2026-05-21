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

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn.frank.ulp.common.entity.account.ThirdPartyUserEntity;
import cn.frank.ulp.common.entity.account.UserIdpBindEntity;

/**
 * 用户身份绑定表
 *
 * @author Frank Zhang
 */
@Repository
public interface UserIdpRepository extends JpaRepository<UserIdpBindEntity, String>,
                                   UserIdpRepositoryCustomized {

    long countByThirdPartyUser(ThirdPartyUserEntity thirdPartyUserEntity);

    /**
     * 根据用户id删除
     *
     * @param userIds {@link String}
     */
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    void deleteAllByUserIdIn(Collection<String> userIds);
}
