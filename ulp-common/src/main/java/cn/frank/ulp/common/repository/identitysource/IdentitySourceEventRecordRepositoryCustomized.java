/*
 * eiam-common - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.frank.ulp.common.repository.identitysource;

import java.util.List;

import cn.frank.ulp.common.entity.identitysource.IdentitySourceEventRecordEntity;

/**
 * 身份源事件记录
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/3/15 21:35
 */
public interface IdentitySourceEventRecordRepositoryCustomized {
    /**
     * 批量保存
     *
     * @param list {@link List}
     */
    void batchSave(List<IdentitySourceEventRecordEntity> list);
}
