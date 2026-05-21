/*
 * eiam-common - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.frank.ulp.common.repository.account;

import java.util.Optional;

import cn.frank.ulp.common.entity.account.po.UserIdpBindPO;
import cn.frank.ulp.support.repository.page.domain.Page;

/**
 * UserIdp Repository Customized
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/12/29 21:27
 */
public interface UserIdpRepositoryCustomized {

    Optional<UserIdpBindPO> selectById(String id);

    /**
     * 根据身份源ID和openId查询
     *
     * @param idpId  {@link  String}
     * @param openId {@link  String}
     * @return {@link Optional}
     */
    Optional<UserIdpBindPO> findByIdpIdAndOpenId(String idpId, String openId);

    /**
     * 根据身份源ID和userId查询
     *
     * @param idpId  {@link  String}
     * @param userId {@link  String}
     * @return {@link Optional}
     */
    Optional<UserIdpBindPO> findByIdpIdAndUserId(String idpId, String userId);

    /**
     * 查询用户身份提供商绑定
     *
     * @param userId {@link  String}
     * @return {@link Page}
     */
    Iterable<UserIdpBindPO> getUserIdpBindList(String userId);
}
