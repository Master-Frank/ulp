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

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import cn.frank.ulp.common.entity.app.po.AppAccessPolicyPO;
import cn.frank.ulp.common.entity.app.query.AppAccessPolicyQueryParam;
import cn.frank.ulp.common.repository.app.AppAccessPolicyRepositoryCustomized;
import cn.frank.ulp.support.exception.TopIamException;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

/**
 * AppPolicy Repository Customized
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/12/29 21:27
 */
@Repository
public class AppAccessPolicyRepositoryCustomizedImpl implements
                                                     AppAccessPolicyRepositoryCustomized {

    /**
     * 获取应用授权策略列表
     *
     * @param pageable {@link  Pageable}
     * @param query    {@link  AppAccessPolicyQueryParam}
     * @return {@link Page}
     */
    @Override
    public Page<AppAccessPolicyPO> getAppPolicyList(AppAccessPolicyQueryParam query,
                                                    Pageable pageable) {
        Map<String, Object> args = new HashMap<>();
        //@formatter:off
        String hql = """
                SELECT
                	NEW cn.frank.ulp.common.entity.app.po.AppAccessPolicyPO(a.id,
                	a.appId,
                	a.subjectId,
                	a.subjectType,
                	a.enabled,
                	a.createTime,
                	subject.name,
                	app.name,
                	app.icon,
                	app.type,
                	app.template,
                	app.protocol)
                FROM
                	AppAccessPolicyEntity a
                	INNER JOIN AppEntity app ON a.appId = app.id
                INNER JOIN
                (
                SELECT
                    id as id,
                	name as name
                FROM
                	UserGroupEntity UNION ALL
                SELECT
                	id as id,
                	name as name
                FROM
                	OrganizationEntity UNION ALL
                SELECT
                	id as id,
                	username AS name
                FROM
                	UserEntity
                	) subject ON a.subjectId = subject.id
                WHERE 1=1
                """;
        //@formatter:on
        String whereSql = "";
        if (ObjectUtils.isNotEmpty(query.getSubjectType())) {
            whereSql += " AND subjectType = :subjectType";
            args.put("subjectType", query.getSubjectType());
        }
        //主体名称
        if (StringUtils.isNotEmpty(query.getSubjectName())) {
            whereSql += " AND subject.name LIKE :subjectName";
            args.put("subjectName", "%" + query.getSubjectName() + "%");
        }

        //主体ID
        if (StringUtils.isNotBlank(query.getSubjectId())) {
            whereSql += " AND a.subjectId = :subjectId";
            args.put("subjectId", query.getSubjectId());
        } else {
            if (StringUtils.isEmpty(query.getAppId())) {
                throw new TopIamException("主体ID不能为空");
            }
        }

        //应用ID
        if (StringUtils.isNotBlank(query.getAppId())) {
            whereSql += " AND a.appId = :appId";
            args.put("appId", query.getAppId());
        }

        //应用名称
        if (StringUtils.isNotBlank(query.getAppName())) {
            whereSql += " AND app.name LIKE :appName";
            args.put("appName", "%" + query.getAppName() + "%");
        }

        //按照创建时间倒序
        TypedQuery<AppAccessPolicyPO> listQuery = entityManager.createQuery(hql + whereSql,
            AppAccessPolicyPO.class);
        args.forEach(listQuery::setParameter);
        listQuery.setFirstResult((int) pageable.getOffset());
        listQuery.setMaxResults(pageable.getPageSize());
        String countSql = """
                SELECT
                	COUNT(DISTINCT a.id)
                FROM
                	AppAccessPolicyEntity a
                	INNER JOIN AppEntity app ON a.appId = app.id
                INNER JOIN
                (
                SELECT
                	id as id,
                	name as name
                FROM
                	UserGroupEntity UNION ALL
                SELECT
                	id as id,
                	name as name
                FROM
                	OrganizationEntity UNION ALL
                SELECT
                	id as id,
                	username AS name
                FROM
                	UserEntity
                	) subject ON a.subjectId = subject.id
                WHERE 1=1
                """;
        TypedQuery<Long> countQuery = entityManager.createQuery(countSql + whereSql, Long.class);
        args.forEach(countQuery::setParameter);
        return new PageImpl<>(listQuery.getResultList(), pageable, countQuery.getSingleResult());
    }

    /**
     * EntityManager
     */
    private final EntityManager entityManager;

    public AppAccessPolicyRepositoryCustomizedImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
