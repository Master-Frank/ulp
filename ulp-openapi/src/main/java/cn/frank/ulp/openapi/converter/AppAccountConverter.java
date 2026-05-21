/*
 * eiam-openapi - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.frank.ulp.openapi.converter;

import java.util.ArrayList;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.util.CollectionUtils;

import cn.frank.ulp.common.entity.app.AppAccountEntity;
import cn.frank.ulp.common.entity.app.po.AppAccountPO;
import cn.frank.ulp.common.entity.app.query.AppAccountQueryParam;
import cn.frank.ulp.openapi.pojo.query.OapiV1AppAccountQuery;
import cn.frank.ulp.openapi.pojo.result.AppAccountListResult;
import cn.frank.ulp.openapi.pojo.save.AppAccountCreateParam;
import cn.frank.ulp.support.repository.page.domain.Page;

/**
 * 应用账户映射
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/6/4 19:08
 */
@Mapper(componentModel = "spring")
public interface AppAccountConverter {

    /**
     * 应用账户分页实体转换应用账户分页结果
     *
     * @param page {@link Page}
     * @return {@link Page}
     */
    default Page<AppAccountListResult> appAccountEntityConvertToAppAccountResult(org.springframework.data.domain.Page<AppAccountPO> page) {
        Page<AppAccountListResult> result = new Page<>();
        if (!CollectionUtils.isEmpty(page.getContent())) {
            List<AppAccountListResult> list = new ArrayList<>();
            for (AppAccountPO po : page.getContent()) {
                list.add(entityConvertToAppAccountResult(po));
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
     * 应用账户实体转换为应用账户结果
     *
     * @param appAccountPo {@link AppAccountPO}
     * @return {@link AppAccountListResult}
     */
    AppAccountListResult entityConvertToAppAccountResult(AppAccountPO appAccountPo);

    /**
     * 应用账户新增参数转换应用账户实体
     *
     * @param param {@link AppAccountCreateParam}
     * @return {@link AppAccountEntity}
     */
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "remark", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "updateBy", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "createBy", ignore = true)
    AppAccountEntity appAccountCreateParamConvertToEntity(AppAccountCreateParam param);

    /**
     * 查询参数转换
     *
     * @param query {@link OapiV1AppAccountQuery}
     * @return {@link AppAccountQueryParam}
     */
    AppAccountQueryParam appAccountQueryToQueryParam(OapiV1AppAccountQuery query);
}
