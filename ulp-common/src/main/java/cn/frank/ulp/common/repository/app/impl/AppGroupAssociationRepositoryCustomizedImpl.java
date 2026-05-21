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
package cn.frank.ulp.common.repository.app.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import cn.frank.ulp.common.entity.app.AppEntity;
import cn.frank.ulp.common.entity.app.query.AppGroupAssociationListQueryParam;
import cn.frank.ulp.common.repository.app.AppGroupAssociationRepositoryCustomized;

import lombok.AllArgsConstructor;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

/**
 * @author Frank Zhang
 */
@Repository
@AllArgsConstructor
public class AppGroupAssociationRepositoryCustomizedImpl implements
                                                         AppGroupAssociationRepositoryCustomized {

    /**
     * 获取应用组应用列表
     *
     * @param query    {@link AppGroupAssociationListQueryParam}
     * @param pageable {@link Pageable}
     * @return {@link Page}
     */
    @SuppressWarnings("DuplicatedCode")
    @Override
    public Page<AppEntity> getAppGroupAssociationList(AppGroupAssociationListQueryParam query,
                                                      Pageable pageable) {
        Map<String, Object> args = new HashMap<>();
        //@formatter:off
        String hql = """
                SELECT DISTINCT
                	 app
                FROM
                	AppEntity app
                	LEFT JOIN AppGroupAssociationEntity ass ON app.id = ass.app.id
                WHERE
                	ass.groupId = :groupId
                """;
        //@formatter:on
        String whereSql = "";
        //用户名
        if (StringUtils.isNoneBlank(query.getAppName())) {
            whereSql += " AND app.name LIKE :name ";
            args.put("name", "%" + query.getAppName() + "%");
        }
        //按照创建时间倒序
        TypedQuery<AppEntity> listQuery = entityManager
            .createQuery(hql + whereSql + " ORDER BY app.createTime DESC", AppEntity.class);
        args.put("groupId", query.getId());
        args.forEach(listQuery::setParameter);
        listQuery.setFirstResult((int) pageable.getOffset());
        listQuery.setMaxResults(pageable.getPageSize());
        String countSql = """
                SELECT
                	COUNT(DISTINCT app.id)
                FROM
                	AppEntity app
                	LEFT JOIN AppGroupAssociationEntity ass ON app.id = ass.app.id
                WHERE
                	ass.groupId = :groupId
                """;
        TypedQuery<Long> countQuery = entityManager.createQuery(countSql + whereSql, Long.class);
        args.forEach(countQuery::setParameter);
        return new PageImpl<>(listQuery.getResultList(), pageable, countQuery.getSingleResult());
    }

    /**
     * EntityManager
     */
    private final EntityManager entityManager;
}
