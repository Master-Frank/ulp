/*
 * eiam-common - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.frank.ulp.common.repository.account;

import java.util.List;

import cn.frank.ulp.common.entity.account.OrganizationEntity;
import cn.frank.ulp.common.entity.account.po.OrganizationPO;

/**
 * @author TopIAM
 * Created by support@topiam.cn on 2022/2/13 21:27
 */
public interface OrganizationRepositoryCustomized {

    /**
     * 批量保存
     *
     * @param list {@link List}
     */
    void batchSave(List<OrganizationEntity> list);

    /**
     * 批量更新
     *
     * @param list {@link List}
     */
    void batchUpdate(List<OrganizationEntity> list);

    /**
     * 用户对应组织列表
     *
     * @param userId {@link  String}
     * @return {@link List}
     */
    List<OrganizationPO> getOrganizationList(String userId);
}
