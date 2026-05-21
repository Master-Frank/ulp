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
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn.frank.ulp.common.entity.account.UserDetailEntity;

/**
 * <p>
 * 用户详情表 Repository 接口
 * </p>
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020-08-07
 */
@Repository
public interface UserDetailRepository extends JpaRepository<UserDetailEntity, String>,
                                      UserDetailRepositoryCustomized {
    /**
     * 根据user id查询用户详情
     *
     * @param user {@link String}
     * @return {@link UserDetailEntity}
     */
    Optional<UserDetailEntity> findByUserId(String user);

    /**
     * 根据用户ID删除用户
     *
     * @param userId {@link  String}
     */
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    void deleteByUserId(@Param("userId") String userId);

    /**
     * 根据用户ID批量删除用户
     *
     * @param userIds {@link List}
     */
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    void deleteAllByUserIdIn(Collection<String> userIds);

    /**
     * 根据用户ID查询用户详情
     *
     * @param userIds  {@link List}
     * @return {@link List}
     */
    List<UserDetailEntity> findAllByUserIdIn(List<String> userIds);
}
