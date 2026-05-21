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
package cn.frank.ulp.common.repository.account.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import cn.frank.ulp.common.entity.account.po.UserPO;
import cn.frank.ulp.common.entity.account.query.UserGroupMemberListQuery;
import cn.frank.ulp.common.repository.account.UserGroupMemberRepositoryCustomized;

import lombok.AllArgsConstructor;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

/**
 * @author Frank Zhang
 */
@Repository
@AllArgsConstructor
public class UserGroupMemberRepositoryCustomizedImpl implements
                                                     UserGroupMemberRepositoryCustomized {
    /**
     * 获取用户组成员列表
     *
     * @param query    {@link UserGroupMemberListQuery}
     * @param pageable {@link Pageable}
     * @return {@link Page}
     */
    @Override
    public Page<UserPO> getUserGroupMemberList(UserGroupMemberListQuery query, Pageable pageable) {
        Map<String, Object> args = new HashMap<>();
        //@formatter:off
        String sql = """
                SELECT
                	%s
                FROM
                	UserGroupMemberEntity ugm
                	INNER JOIN UserEntity u ON ugm.userId = u.id
                	INNER JOIN UserGroupEntity ug ON ug.id = ugm.groupId
                	LEFT  JOIN OrganizationMemberEntity om ON u.id = om.userId
                    LEFT  JOIN OrganizationEntity o ON o.id = om.orgId
                """;
        args.put("userGroupMemberId", query.getId());
        StringBuilder whereSql = new StringBuilder("""
                WHERE
                	ugm.groupId = :userGroupMemberId
                	AND ug.id = :userGroupMemberId
                """);
        //用户名
        if (StringUtils.isNoneBlank(query.getFullName())) {
            whereSql.append(" AND u.fullName LIKE :fullName ");
            args.put("fullName", "%" + query.getFullName() + "%");
        }
        //@formatter:on
        String listSql = sql.formatted("""
                NEW cn.frank.ulp.common.entity.account.po.UserPO(u,
                LISTAGG(o.displayPath , ',' ))
                """);
        TypedQuery<UserPO> listQuery = entityManager
            .createQuery(listSql + whereSql + " GROUP BY u.id ", UserPO.class);
        args.forEach(listQuery::setParameter);
        listQuery.setFirstResult((int) pageable.getOffset());
        listQuery.setMaxResults(pageable.getPageSize());
        String countSql = sql.formatted("COUNT(DISTINCT u.id)");
        TypedQuery<Long> countQuery = entityManager.createQuery(countSql + whereSql, Long.class);
        args.forEach(countQuery::setParameter);
        return new PageImpl<>(listQuery.getResultList(), pageable, countQuery.getSingleResult());
    }

    private final EntityManager entityManager;
}
