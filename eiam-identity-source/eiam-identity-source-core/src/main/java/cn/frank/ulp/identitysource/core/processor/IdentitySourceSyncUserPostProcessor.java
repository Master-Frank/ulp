/*
 * eiam-identity-source-core - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.frank.ulp.identitysource.core.processor;

import java.time.LocalDateTime;
import java.util.List;

import cn.frank.ulp.common.enums.TriggerType;
import cn.frank.ulp.identitysource.core.domain.User;

/**
 * 身份源数据 pull post 处理器
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/3/1 22:04
 */
public interface IdentitySourceSyncUserPostProcessor {
    /**
     * 处理数据
     *
     * @param batch {@link String}
     * @param identitySourceId {@link String}
     * @param userList {@link  List}
     * @param startTime {@link  LocalDateTime}
     * @param triggerType {@link  TriggerType}
     */
    void process(String batch, String identitySourceId, List<User> userList,
                 LocalDateTime startTime, TriggerType triggerType);

}
