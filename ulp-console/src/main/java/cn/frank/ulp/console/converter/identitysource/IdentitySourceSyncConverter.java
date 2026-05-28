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

import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

import cn.frank.ulp.common.entity.account.UserGroupEntity;
import cn.frank.ulp.common.entity.identitysource.IdentitySourceSyncHistoryEntity;
import cn.frank.ulp.common.entity.identitysource.IdentitySourceSyncRecordEntity;
import cn.frank.ulp.console.pojo.query.identity.IdentitySourceSyncHistoryListQuery;
import cn.frank.ulp.console.pojo.query.identity.IdentitySourceSyncRecordListQuery;
import cn.frank.ulp.console.pojo.result.account.UserGroupListResult;
import cn.frank.ulp.console.pojo.result.identitysource.IdentitySourceSyncHistoryListResult;
import cn.frank.ulp.console.pojo.result.identitysource.IdentitySourceSyncRecordListResult;
import cn.frank.ulp.support.repository.page.domain.Page;

import jakarta.persistence.criteria.Predicate;
import static cn.frank.ulp.common.entity.identitysource.IdentitySourceSyncHistoryEntity.*;
import static cn.frank.ulp.support.repository.base.BaseEntity.LAST_MODIFIED_TIME;

/**
 * 身份源转换器
 *
 * @author Frank Zhang
 */
@Mapper(componentModel = "spring")
public interface IdentitySourceSyncConverter {

    /**
     * 身份源同步列表参数转换为  Specification
     *
     * @param listQuery {@link IdentitySourceSyncHistoryListQuery} query
     * @return {@link Specification}
     */
    default Specification<IdentitySourceSyncHistoryEntity> queryIdentitySourceSyncHistoryListQueryConvertToSpecification(IdentitySourceSyncHistoryListQuery listQuery) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get(IDENTITY_SOURCE_ID_FIELD_NAME),
                listQuery.getIdentitySourceId()));
            if (Objects.nonNull(listQuery.getTriggerType())) {
                predicates.add(criteriaBuilder.equal(root.get(TRIGGER_TYPE_FIELD_NAME),
                    listQuery.getTriggerType()));
            }
            if (Objects.nonNull(listQuery.getStatus())) {
                predicates
                    .add(criteriaBuilder.equal(root.get(STATUS_FIELD_NAME), listQuery.getStatus()));
            }
            if (Objects.nonNull(listQuery.getObjectType())) {
                predicates.add(criteriaBuilder.equal(root.get(OBJECT_TYPE_FIELD_NAME),
                    listQuery.getObjectType()));
            }
            query.where(predicates.toArray(new Predicate[0]));
            query.orderBy(criteriaBuilder.desc(root.get(LAST_MODIFIED_TIME)));
            return query.getRestriction();
        };
    }

    /**
     * 身份源同步实体转换为身份源同步分页结果
     *
     * @param page {@link Page}
     * @return {@link Page}
     */
    default Page<IdentitySourceSyncHistoryListResult> entityConvertToIdentitySourceSyncHistoryListResult(org.springframework.data.domain.Page<IdentitySourceSyncHistoryEntity> page) {
        Page<IdentitySourceSyncHistoryListResult> result = new Page<>();
        if (!CollectionUtils.isEmpty(page.getContent())) {
            List<IdentitySourceSyncHistoryListResult> list = new ArrayList<>();
            for (IdentitySourceSyncHistoryEntity entity : page.getContent()) {
                list.add(entityConvertToIdentitySourceSyncHistoryListResult(entity));
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
     * 身份源同步转换为身份源同步分页结果
     *
     * @param entity {@link UserGroupEntity}
     * @return {@link UserGroupListResult}
     */
    default IdentitySourceSyncHistoryListResult entityConvertToIdentitySourceSyncHistoryListResult(IdentitySourceSyncHistoryEntity entity) {
        if (entity == null) {
            return null;
        }

        IdentitySourceSyncHistoryListResult identitySourceSyncHistoryListResult = new IdentitySourceSyncHistoryListResult();

        if (entity.getId() != null) {
            identitySourceSyncHistoryListResult.setId(String.valueOf(entity.getId()));
        }
        identitySourceSyncHistoryListResult.setBatch(entity.getBatch());
        if (entity.getIdentitySourceId() != null) {
            identitySourceSyncHistoryListResult
                .setIdentitySourceId(String.valueOf(entity.getIdentitySourceId()));
        }
        if (entity.getCreatedCount() != null) {
            identitySourceSyncHistoryListResult
                .setCreatedCount(String.valueOf(entity.getCreatedCount()));
        }
        if (entity.getUpdatedCount() != null) {
            identitySourceSyncHistoryListResult
                .setUpdatedCount(String.valueOf(entity.getUpdatedCount()));
        }
        if (entity.getSkippedCount() != null) {
            identitySourceSyncHistoryListResult
                .setSkippedCount(String.valueOf(entity.getSkippedCount()));
        }
        if (entity.getDeletedCount() != null) {
            identitySourceSyncHistoryListResult
                .setDeletedCount(String.valueOf(entity.getDeletedCount()));
        }
        identitySourceSyncHistoryListResult.setStartTime(entity.getStartTime());
        identitySourceSyncHistoryListResult.setEndTime(entity.getEndTime());
        identitySourceSyncHistoryListResult.setObjectType(entity.getObjectType());
        identitySourceSyncHistoryListResult.setTriggerType(entity.getTriggerType());
        if (entity.getStatus() != null) {
            identitySourceSyncHistoryListResult.setStatus(entity.getStatus().getCode());
        }
        if (entity.getEndTime() != null) {
            identitySourceSyncHistoryListResult.setSpendTime(
                String.valueOf(
                    entity.getEndTime().toInstant(java.time.ZoneOffset.of("+8")).getEpochSecond()
                               - entity.getStartTime().toInstant(java.time.ZoneOffset.of("+8"))
                                   .getEpochSecond()));
        }
        return identitySourceSyncHistoryListResult;
    }

    /**
     * 查询身份源同步详情列表参数转换为 Specification
     *
     * @param listQuery {@link IdentitySourceSyncRecordListQuery} query
     * @return {@link Specification}
     */
    default Specification<IdentitySourceSyncRecordEntity> queryIdentitySourceSyncRecordListQueryConvertToSpecification(IdentitySourceSyncRecordListQuery listQuery) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (Objects.nonNull(listQuery.getStatus())) {
                predicates.add(criteriaBuilder.equal(root.get("status"), listQuery.getStatus()));
            }
            if (StringUtils.isNotBlank(listQuery.getSyncHistoryId())) {
                predicates.add(
                    criteriaBuilder.equal(root.get("syncHistoryId"), listQuery.getSyncHistoryId()));
            }
            if (Objects.nonNull(listQuery.getObjectType())) {
                predicates
                    .add(criteriaBuilder.equal(root.get("objectType"), listQuery.getObjectType()));
            }
            if (Objects.nonNull(listQuery.getActionType())) {
                predicates
                    .add(criteriaBuilder.equal(root.get("actionType"), listQuery.getActionType()));
            }
            query.where(predicates.toArray(new Predicate[0]));
            query.orderBy(criteriaBuilder.desc(root.get(LAST_MODIFIED_TIME)));
            return query.getRestriction();
        };
    }

    /**
     * 身份源同步详情转换为身份源详情分页结果
     *
     * @param page {@link Page}
     * @return {@link Page}
     */
    default Page<IdentitySourceSyncRecordListResult> entityConvertToIdentitySourceSyncRecordListResult(org.springframework.data.domain.Page<IdentitySourceSyncRecordEntity> page) {
        Page<IdentitySourceSyncRecordListResult> result = new Page<>();
        if (!CollectionUtils.isEmpty(page.getContent())) {
            List<IdentitySourceSyncRecordListResult> list = new ArrayList<>();
            for (IdentitySourceSyncRecordEntity entity : page.getContent()) {
                list.add(entityConvertToIdentitySourceSyncRecordListResult(entity));
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
     * 身份源同步详情实体转换为身份源同步详情分页结果
     *
     * @param page {@link UserGroupEntity}
     * @return {@link UserGroupListResult}
     */
    @Mapping(target = "status", source = "status.code")
    IdentitySourceSyncRecordListResult entityConvertToIdentitySourceSyncRecordListResult(IdentitySourceSyncRecordEntity page);
}
