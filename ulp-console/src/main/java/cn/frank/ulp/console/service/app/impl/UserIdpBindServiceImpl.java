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
package cn.frank.ulp.console.service.app.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cn.frank.ulp.audit.context.AuditContext;
import cn.frank.ulp.audit.entity.Target;
import cn.frank.ulp.audit.enums.TargetType;
import cn.frank.ulp.common.entity.account.po.UserIdpBindPO;
import cn.frank.ulp.common.repository.account.UserIdpRepository;
import cn.frank.ulp.console.converter.app.UserIdpBindConverter;
import cn.frank.ulp.console.pojo.result.app.UserIdpBindListResult;
import cn.frank.ulp.console.service.app.UserIdpBindService;
import cn.frank.ulp.support.exception.UlpException;
import cn.frank.ulp.support.repository.page.domain.Page;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 用户身份提供商绑定
 *
 * @author Frank Zhang
 */
@Component
@Slf4j
@AllArgsConstructor
public class UserIdpBindServiceImpl implements UserIdpBindService {
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean unbindUserIdpBind(String id) {
        Optional<UserIdpBindPO> optional = userIdpRepository.selectById(id);
        //用户不存在
        if (optional.isEmpty()) {
            AuditContext.setContent("解绑失败，用户身份提供商绑定关系不存在");
            log.warn(AuditContext.getContent());
            throw new UlpException(AuditContext.getContent());
        }
        UserIdpBindPO bind = optional.get();
        userIdpRepository.deleteById(id);
        //@formatter:off
        List<Target> targets= new ArrayList<>();
        targets.add(Target.builder().id(bind.getUserId()).name(bind.getUserName()).type(TargetType.USER).build());
        targets.add(Target.builder().id(bind.getIdpId()).name(bind.getIdpName()).type(TargetType.IDENTITY_PROVIDER).build());
        AuditContext.setTarget(targets);
        //@formatter:on
        return true;
    }

    /**
     * 查询用户身份提供商绑定
     *
     * @param userId     {@link  String}
     * @return {@link Page}
     */
    @Override
    public List<UserIdpBindListResult> getUserIdpBindList(String userId) {
        //查询映射
        return userIdpBindConverter.userIdpBindEntityConvertToUserIdpBindListResult(
            userIdpRepository.getUserIdpBindList(userId));
    }

    /**
     * UserIdpBindConverter
     */
    private final UserIdpBindConverter userIdpBindConverter;

    /**
     * UserIdpRepositoryCustomizedImpl
     */
    private final UserIdpRepository    userIdpRepository;
}
