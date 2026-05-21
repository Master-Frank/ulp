/*
 * ulp-audit - United Login Platform
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
package cn.frank.ulp.audit.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import cn.frank.ulp.audit.endpoint.pojo.AuditListQuery;
import cn.frank.ulp.audit.endpoint.pojo.AuditListResult;
import cn.frank.ulp.audit.endpoint.pojo.DictResult;
import cn.frank.ulp.audit.entity.AuditEntity;
import cn.frank.ulp.audit.event.type.EventType;
import cn.frank.ulp.audit.repository.AuditRepository;
import cn.frank.ulp.audit.service.AuditService;
import cn.frank.ulp.audit.service.converter.AuditDataConverter;
import cn.frank.ulp.support.exception.BadParamsException;
import cn.frank.ulp.support.repository.page.domain.Page;
import cn.frank.ulp.support.repository.page.domain.PageModel;
import cn.frank.ulp.support.security.userdetails.UserType;
import cn.frank.ulp.support.security.util.SecurityUtils;

import lombok.RequiredArgsConstructor;
import static cn.frank.ulp.support.security.userdetails.UserType.USER;

/**
 * 审计 service impl
 *
 * @author Frank Zhang
 */
@Service
@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService {

    /**
     * List
     *
     * @param query {@link AuditListQuery}
     * @param page {@link PageModel}
     * @return {@link Page}
     */
    @Override
    public Page<AuditListResult> getAuditList(AuditListQuery query, PageModel page) {
        if (USER.equals(SecurityUtils.getCurrentUser().getUserType())
            && !USER.getType().equals(query.getUserType())) {
            throw new BadParamsException("用户类型错误");
        }
        //查询入参转查询条件
        Specification<AuditEntity> specification = auditDataConverter
            .auditListRequestConvertToSpecification(query, page);
        if (Objects.isNull(specification)) {
            return new Page<>();
        }
        // 排序
        List<Sort.Order> orders = new ArrayList<>();
        for (PageModel.Sort sort : page.getSorts()) {
            orders.add(new Sort.Order((sort.getAsc() ? Sort.Direction.ASC : Sort.Direction.DESC),
                sort.getSorter()));
        }
        //分页条件
        PageRequest request = PageRequest.of(page.getCurrent(), page.getPageSize(),
            Sort.by(orders));
        //查询列表
        return auditDataConverter
            .entityConvertToAuditListResult(auditRepository.findAll(specification, request), page);
    }

    /**
     * 获取字典类型
     *
     * @param userType {@link String}
     * @return {@link List}
     */
    @Override
    public List<DictResult> getAuditDict(String userType) {
        List<EventType> types = Arrays.asList(EventType.values());
        //获取分组
        List<DictResult> list = types.stream().map(EventType::getResource).toList().stream()
            .distinct().toList().stream().map(resource -> {
                DictResult group = new DictResult();
                group.setName(resource.getName());
                group.setCode(resource.getCode());
                return group;
            }).collect(Collectors.toList());
        //处理每个分组的审计类型
        list.forEach(dict -> {
            Set<DictResult.AuditType> auditTypes = new LinkedHashSet<>();
            types.stream()
                .filter(auditType -> auditType.getResource().getCode().equals(dict.getCode()))
                .forEach(auditType -> {
                    if (auditType.getUserTypes().stream().map(UserType::getType).toList()
                        .contains(userType)) {
                        DictResult.AuditType type = new DictResult.AuditType();
                        type.setName(auditType.getDesc());
                        type.setCode(auditType.getCode());
                        auditTypes.add(type);
                    }
                });
            dict.setTypes(auditTypes);
        });
        list = list.stream().filter(i -> !CollectionUtils.isEmpty(i.getTypes()))
            .collect(Collectors.toList());
        return list;
    }

    /**
     * AuditDataConverter
     */
    private final AuditDataConverter auditDataConverter;

    /**
     * AuditRepository
     */
    private final AuditRepository    auditRepository;
}
