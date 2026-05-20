/*
 * eiam-common - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.frank.ulp.common.repository.account;

import java.util.List;

import cn.frank.ulp.common.entity.account.OrganizationMemberEntity;

/**
 * 组织成员
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/10/2 02:53
 */
public interface OrganizationMemberCustomizedRepository {

    /**
     * 批量保存
     *
     * @param list {@link String}
     */
    void batchSave(List<OrganizationMemberEntity> list);
}
