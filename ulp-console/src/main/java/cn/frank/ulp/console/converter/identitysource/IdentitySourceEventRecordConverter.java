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
package cn.frank.ulp.console.converter.identitysource;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.mapstruct.Mapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

import cn.frank.ulp.common.entity.account.UserGroupEntity;
import cn.frank.ulp.common.entity.identitysource.IdentitySourceEventRecordEntity;
import cn.frank.ulp.console.pojo.query.identity.IdentitySourceEventRecordListQuery;
import cn.frank.ulp.console.pojo.result.account.UserGroupListResult;
import cn.frank.ulp.console.pojo.result.identitysource.IdentitySourceEventRecordListResult;
import cn.frank.ulp.support.context.ApplicationContextService;
import cn.frank.ulp.support.repository.page.domain.Page;

import jakarta.persistence.criteria.Predicate;
import static cn.frank.ulp.common.entity.identitysource.IdentitySourceEventRecordEntity.*;
import static cn.frank.ulp.support.repository.base.BaseEntity.LAST_MODIFIED_TIME;

/**
 * 身份源事件记录转换器
 *
 * @author Frank Zhang
 */
@Mapper(componentModel = "spring")
public interface IdentitySourceEventRecordConverter {

    /**
     * 身份源事件记录列表参数转换为  Specification
     *
     * @param listQuery {@link IdentitySourceEventRecordListQuery} listQuery
     * @return {@link Specification}
     */
    default Specification<IdentitySourceEventRecordEntity> queryIdentitySourceEventRecordListQueryConvertToSpecification(IdentitySourceEventRecordListQuery listQuery) {
        //查询条件
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get(IDENTITY_SOURCE_ID_FIELD_NAME),
                listQuery.getIdentitySourceId()));
            if (Objects.nonNull(listQuery.getActionType())) {
                predicates.add(criteriaBuilder.equal(root.get(ACTION_TYPE_FIELD_NAME),
                    listQuery.getActionType()));
            }
            if (Objects.nonNull(listQuery.getObjectType())) {
                predicates.add(criteriaBuilder.equal(root.get(OBJECT_TYPE_FIELD_NAME),
                    listQuery.getObjectType()));
            }
            if (Objects.nonNull(listQuery.getStatus())) {
                predicates
                    .add(criteriaBuilder.equal(root.get(STATUS_FIELD_NAME), listQuery.getStatus()));
            }
            query.where(predicates.toArray(new Predicate[0]));
            query.orderBy(criteriaBuilder.desc(root.get(LAST_MODIFIED_TIME)));
            return query.getRestriction();
        };
    }

    /**
     * 身份源事件记录实体转换为身份源事件记录分页结果
     *
     * @param page {@link Page}
     * @return {@link Page}
     */
    default Page<IdentitySourceEventRecordListResult> entityConvertToIdentitySourceSyncRecordListResult(org.springframework.data.domain.Page<IdentitySourceEventRecordEntity> page) {
        Page<IdentitySourceEventRecordListResult> result = new Page<>();
        if (!CollectionUtils.isEmpty(page.getContent())) {
            List<IdentitySourceEventRecordListResult> list = new ArrayList<>();
            for (IdentitySourceEventRecordEntity entity : page.getContent()) {
                IdentitySourceEventRecordConverter bean = ApplicationContextService
                    .getBean(IdentitySourceEventRecordConverter.class);
                list.add(bean.entityConvertToIdentitySourceSyncRecordListResult(entity));
            }

            //@formatter:off
            result.setPagination(Page.Pagination.builder()
                    .total(page.getTotalElements())
                    .totalPages(page.getTotalPages())
                    .current(page.getPageable().getPageNumber() + 1)
                    .build());
            //@formatter:on
            result.setList(list);
        }
        return result;
    }

    /**
     * 身份源事件记录转换为身份源事件记录分页结果
     *
     * @param entity {@link UserGroupEntity}
     * @return {@link UserGroupListResult}
     */
    IdentitySourceEventRecordListResult entityConvertToIdentitySourceSyncRecordListResult(IdentitySourceEventRecordEntity entity);
}
