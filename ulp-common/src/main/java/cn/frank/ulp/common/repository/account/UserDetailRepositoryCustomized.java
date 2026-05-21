/*
 * eiam-common - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.frank.ulp.common.repository.account;

import java.util.ArrayList;
import java.util.List;

import cn.frank.ulp.common.entity.account.UserDetailEntity;

/**
 * User Detail Repository Customized
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/12/29 21:27
 */
public interface UserDetailRepositoryCustomized {
    /**
     * 批量新增
     *
     * @param list {@link List}
     */
    void batchSave(List<UserDetailEntity> list);

    /**
     * 批量更新
     *
     * @param list {@link List}
     */
    void batchUpdate(ArrayList<UserDetailEntity> list);
}
