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
package cn.frank.ulp.console.converter.setting;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

import cn.frank.ulp.common.entity.account.query.UserListQuery;
import cn.frank.ulp.common.entity.setting.AdministratorEntity;
import cn.frank.ulp.common.enums.UserStatus;
import cn.frank.ulp.console.pojo.query.setting.AdministratorListQuery;
import cn.frank.ulp.console.pojo.result.setting.AdministratorListResult;
import cn.frank.ulp.console.pojo.result.setting.AdministratorResult;
import cn.frank.ulp.console.pojo.save.setting.AdministratorCreateParam;
import cn.frank.ulp.console.pojo.update.setting.AdministratorUpdateParam;
import cn.frank.ulp.support.context.ApplicationContextService;
import cn.frank.ulp.support.repository.page.domain.Page;
import cn.frank.ulp.support.util.BeanUtils;

import jakarta.persistence.criteria.Predicate;
import static cn.frank.ulp.common.entity.setting.AdministratorEntity.*;
import static cn.frank.ulp.support.repository.base.BaseEntity.LAST_MODIFIED_BY;
import static cn.frank.ulp.support.repository.base.BaseEntity.LAST_MODIFIED_TIME;
import static cn.frank.ulp.support.util.AvatarUtils.bufferedImageToBase64;
import static cn.frank.ulp.support.util.AvatarUtils.generateAvatarImg;
import static cn.frank.ulp.support.util.PhoneUtils.getPhoneAreaCode;
import static cn.frank.ulp.support.util.PhoneUtils.getPhoneNumber;

/**
 * 管理员映射
 *
 * @author Frank Zhang
 */
@Mapper(componentModel = "spring")
public interface AdministratorConverter {

    /**
     * 管理员实体转换为管理员分页结果
     *
     * @param page {@link Page}
     * @return {@link Page}
     */
    default Page<AdministratorListResult> entityConvertToAdministratorPaginationResult(org.springframework.data.domain.Page<AdministratorEntity> page) {
        Page<AdministratorListResult> result = new Page<>();
        if (!CollectionUtils.isEmpty(page.getContent())) {
            List<AdministratorListResult> list = new ArrayList<>();
            for (AdministratorEntity user : page.getContent()) {
                AdministratorListResult convert = entityConvertToAdministratorPaginationResult(
                    user);
                //头像
                if (StringUtils.isEmpty(user.getAvatar())) {
                    convert.setAvatar(bufferedImageToBase64(generateAvatarImg(user.getUsername())));
                } else {
                    convert.setAvatar(user.getAvatar());
                }
                if (org.springframework.util.StringUtils.hasText(convert.getPhone())) {
                    convert.setPhone(
                        (org.springframework.util.StringUtils.hasText(user.getPhoneAreaCode())
                            ? "+" + user.getPhoneAreaCode()
                            : "") + convert.getPhone());
                }
                list.add(convert);
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
     * 管理员实体转换为管理员分页结果
     *
     * @param page {@link AdministratorEntity}
     * @return {@link AdministratorListResult}
     */
    @Mapping(target = "initialized", expression = "java(page.getUsername().equals(cn.frank.ulp.support.constant.UlpConstants.DEFAULT_ADMIN_USERNAME))")
    @Mapping(target = "status", source = "status.code")
    @Mapping(target = "emailVerified", source = "emailVerified", defaultValue = "false")
    @Mapping(target = "authTotal", source = "authTotal", defaultValue = "0L")
    AdministratorListResult entityConvertToAdministratorPaginationResult(AdministratorEntity page);

    /**
     * 管理员创建参数转换为管理员实体
     *
     * @param param {@link AdministratorCreateParam}
     * @return {@link AdministratorEntity}
     */
    default AdministratorEntity administratorCreateParamConvertToEntity(AdministratorCreateParam param) {
        if (param == null) {
            return null;
        }

        AdministratorEntity entity = new AdministratorEntity();

        entity.setRemark(param.getRemark());
        entity.setUsername(param.getUsername());
        entity.setPassword(param.getPassword());
        //邮箱
        if (org.springframework.util.StringUtils.hasText(param.getEmail())) {
            entity.setEmail(param.getEmail());
            entity.setEmailVerified(Boolean.TRUE);
        }
        //手机号
        if (org.springframework.util.StringUtils.hasText(param.getPhone())) {
            entity.setPhone(getPhoneNumber(param.getPhone()));
            entity.setPhoneVerified(Boolean.TRUE);
            entity.setPhoneAreaCode(getPhoneAreaCode(param.getPhone()));
        }
        entity.setStatus(UserStatus.ENABLED);
        entity.setAuthTotal(0L);
        entity.setLastUpdatePasswordTime(java.time.LocalDateTime.now());
        //密码处理
        entity.setPassword(ApplicationContextService
            .getBean(org.springframework.security.crypto.password.PasswordEncoder.class)
            .encode(param.getPassword()));
        return entity;
    }

    /**
     * 管理员更新参数转换为管理员实体类
     *
     * @param param {@link AdministratorUpdateParam}  更新参数
     * @param target {@link AdministratorUpdateParam} 查询实体
     * @return {@link AdministratorEntity} 管理员实体
     */
    default AdministratorEntity administratorUpdateParamConvertToEntity(AdministratorUpdateParam param,
                                                                        AdministratorEntity target) {
        if (param == null) {
            return null;
        }

        AdministratorEntity entity = new AdministratorEntity();
        if (param.getId() != null) {
            entity.setId(param.getId());
        }
        entity.setRemark(param.getRemark());
        //邮箱
        if (StringUtils.isNotEmpty(param.getEmail())) {
            entity.setEmail(param.getEmail());
            entity.setEmailVerified(Boolean.TRUE);
        }
        //手机号
        if (StringUtils.isNotEmpty(param.getPhone())) {
            entity.setPhone(getPhoneNumber(param.getPhone()));
            entity.setPhoneVerified(Boolean.TRUE);
            entity.setPhoneAreaCode(getPhoneAreaCode(param.getPhone()));
        }
        entity.setAvatar(param.getAvatar());
        BeanUtils.merge(entity, target, LAST_MODIFIED_TIME, LAST_MODIFIED_BY);
        if (StringUtils.isBlank(entity.getPhone())) {
            target.setPhone(null);
            target.setPhoneVerified(Boolean.FALSE);
            target.setPhoneAreaCode("");
        }
        if (StringUtils.isBlank(entity.getEmail())) {
            target.setEmail("");
            target.setEmailVerified(Boolean.FALSE);
        }
        return target;
    }

    /**
     * 实体转为管理员详情返回
     *
     * @param user {@link AdministratorEntity}
     * @return {@link AdministratorResult} 管理员详情
     */
    @Mapping(target = "status", source = "status.code")
    @Mapping(target = "initialized", expression = "java(user.getUsername().equals(cn.frank.ulp.support.constant.UlpConstants.DEFAULT_ADMIN_USERNAME))")
    AdministratorResult entityConvertToAdministratorDetailsResult(AdministratorEntity user);

    /**
     * 查询管理员列表参数转换为 Specification
     *
     * @param listQuery {@link UserListQuery} listQuery
     * @return {@link Specification}
     */
    default Specification<AdministratorEntity> queryAdministratorListParamConvertToSpecification(AdministratorListQuery listQuery) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.isNotBlank(listQuery.getUsername())) {
                predicates.add(criteriaBuilder.equal(root.get(USERNAME_FIELD_NAME),
                    "%" + listQuery.getUsername() + "%"));
            }
            if (StringUtils.isNotBlank(listQuery.getPhone())) {
                predicates.add(
                    criteriaBuilder.equal(root.get(PHONE_FIELD_NAME), listQuery.getUsername()));
            }
            if (StringUtils.isNotBlank(listQuery.getEmail())) {
                predicates.add(
                    criteriaBuilder.equal(root.get(EMAIL_FIELD_NAME), listQuery.getUsername()));
            }
            query.where(predicates.toArray(new Predicate[0]));
            query.orderBy(criteriaBuilder.desc(root.get(LAST_MODIFIED_TIME)));
            return query.getRestriction();
        };
    }

}
