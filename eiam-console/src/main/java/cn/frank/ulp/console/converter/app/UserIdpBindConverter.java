/*
 * eiam-console - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.frank.ulp.console.converter.app;

import java.util.ArrayList;
import java.util.List;

import org.mapstruct.Mapper;

import cn.frank.ulp.common.entity.account.po.UserIdpBindPO;
import cn.frank.ulp.console.pojo.result.app.UserIdpBindListResult;
import cn.frank.ulp.support.repository.page.domain.Page;

/**
 * 用户身份提供商绑定
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/11/3 21:08
 */
@Mapper(componentModel = "spring")
public interface UserIdpBindConverter {

    /**
     * 用户身份提供商绑定关系分页结果
     *
     * @param page {@link Page}
     * @return {@link Page}
     */
    default List<UserIdpBindListResult> userIdpBindEntityConvertToUserIdpBindListResult(Iterable<UserIdpBindPO> page) {
        List<UserIdpBindListResult> list = new ArrayList<>();
        for (UserIdpBindPO entity : page) {
            list.add(entityConvertToAppAccountResult(entity));
        }
        return list;
    }

    /**
     * 用户身份提供商绑定关系转换结果
     *
     * @param userIdpBindPo {@link UserIdpBindPO}
     * @return {@link UserIdpBindListResult}
     */
    UserIdpBindListResult entityConvertToAppAccountResult(UserIdpBindPO userIdpBindPo);
}
