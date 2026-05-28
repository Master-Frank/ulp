/*
 * ulp-portal - United Login Platform
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
package cn.frank.ulp.portal.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cn.frank.ulp.authentication.common.IdentityProviderAuthenticationService;
import cn.frank.ulp.authentication.common.authentication.IdentityProviderUserDetails;
import cn.frank.ulp.authentication.common.exception.UserBindIdentityProviderException;
import cn.frank.ulp.common.entity.account.ThirdPartyUserEntity;
import cn.frank.ulp.common.entity.account.UserEntity;
import cn.frank.ulp.common.entity.account.UserIdpBindEntity;
import cn.frank.ulp.common.entity.account.po.UserIdpBindPO;
import cn.frank.ulp.common.entity.authn.IdentityProviderEntity;
import cn.frank.ulp.common.repository.account.ThirdPartyUserRepository;
import cn.frank.ulp.common.repository.account.UserIdpRepository;
import cn.frank.ulp.common.repository.account.UserRepository;
import cn.frank.ulp.common.repository.authentication.IdentityProviderRepository;
import cn.frank.ulp.portal.converter.AccountConverter;
import cn.frank.ulp.support.exception.UlpException;
import cn.frank.ulp.support.security.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 身份验证用户详细信息实现
 *
 * @author Frank Zhang
 */
@Component
@Slf4j
@AllArgsConstructor
public class IdentityProviderAuthenticationServiceImpl implements
                                                       IdentityProviderAuthenticationService {
    /**
     * 用户是否绑定
     *
     * @param openId     {@link String}
     * @param providerId {@link String}
     * @return {@link Boolean}
     */
    @Override
    public Boolean checkIdpUserIsExistBind(String openId, String providerId) {
        Optional<IdentityProviderEntity> source = identityProviderRepository.findById(providerId);
        if (source.isEmpty()) {
            throw new NullPointerException("认证源不存在");
        }
        if (!source.get().getEnabled()) {
            throw new UlpException("认证源已禁用");
        }
        Optional<UserIdpBindPO> authnBind = userIdpRepository.findByIdpIdAndOpenId(providerId,
            openId);
        return authnBind.isPresent();
    }

    /**
     * 绑定
     *
     * @param accountId   {@link  String} 账户ID
     * @param identityProviderUserDetails {@link  IdentityProviderUserDetails} 用户信息
     * @return {@link  Boolean}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean bindUserIdp(String accountId,
                               IdentityProviderUserDetails identityProviderUserDetails) {
        // 查询是否已绑定其他账号
        Optional<ThirdPartyUserEntity> thirdPartyUserEntity = this.thirdPartyUserRepository
            .findByOpenIdAndIdpId(identityProviderUserDetails.getOpenId(),
                identityProviderUserDetails.getProviderId());
        ThirdPartyUserEntity saveThirdPartyUser = accountConverter
            .thirdPartyUserConverterToEntity(identityProviderUserDetails);
        if (thirdPartyUserEntity.isPresent()) {
            long existence = userIdpRepository.countByThirdPartyUser(thirdPartyUserEntity.get());
            if (existence > 0) {
                throw new UserBindIdentityProviderException("该三方账号已绑定其他账号",
                    HttpStatus.INTERNAL_SERVER_ERROR);
            }
            ThirdPartyUserEntity thirdPartyUser = thirdPartyUserEntity.get();
            saveThirdPartyUser.setId(thirdPartyUser.getId());
            // 更新三方用户
            if (!thirdPartyUser.equals(saveThirdPartyUser)) {
                thirdPartyUserRepository.save(saveThirdPartyUser);
            }
        } else {
            thirdPartyUserRepository.save(saveThirdPartyUser);
        }
        // 创建绑定关系
        UserIdpBindEntity userIdpBind = new UserIdpBindEntity();
        userIdpBind.setUserId(accountId);
        userIdpBind.setThirdPartyUser(saveThirdPartyUser);
        userIdpBind.setBindTime(LocalDateTime.now());
        userIdpRepository.save(userIdpBind);
        return true;
    }

    /**
     * 更新三方账户信息
     *
     * @param identityProviderUserDetails   {@link IdentityProviderUserDetails}
     * @param providerId {@link String}
     * @return {@link Boolean}
     */
    @Override
    public Boolean updateThirdPartyUser(IdentityProviderUserDetails identityProviderUserDetails,
                                        String providerId) {
        Optional<ThirdPartyUserEntity> thirdPartyUser = thirdPartyUserRepository
            .findByOpenIdAndIdpId(identityProviderUserDetails.getOpenId(), providerId);
        if (thirdPartyUser.isPresent()) {
            // 判断是否需要更新
            ThirdPartyUserEntity entity = thirdPartyUser.get();
            ThirdPartyUserEntity newThirdPartyUser = accountConverter
                .thirdPartyUserConverterToEntity(identityProviderUserDetails);
            newThirdPartyUser.setId(entity.getId());
            if (!entity.equals(newThirdPartyUser)) {
                thirdPartyUserRepository.save(newThirdPartyUser);
            }
            return Boolean.TRUE;
        }
        log.error("三方账号信息不存在, openId:[{}], idpId:[{}]", identityProviderUserDetails.getOpenId(),
            providerId);
        return Boolean.FALSE;
    }

    /**
     * 获取用户
     *
     * @param openId     {@link  String}
     * @param providerId {@link  String}
     * @return {@link  UserDetails}
     */
    @Override
    public UserDetails getUserDetails(String openId, String providerId) {
        // 权限
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        UserEntity user = getUser(openId, providerId);
        return user.toUserDetails(authorities);
    }

    private UserEntity getUser(String openId, String providerId) {
        Optional<UserIdpBindPO> optional = userIdpRepository.findByIdpIdAndOpenId(providerId,
            openId);
        if (optional.isEmpty()) {
            throw new NullPointerException("用户未绑定");
        }
        UserIdpBindPO userIdpBind = optional.get();
        String userId = userIdpBind.getUserId();
        Optional<UserEntity> entity = userRepository.findById(userId);
        if (entity.isEmpty()) {
            throw new NullPointerException("用户不存在");
        }
        return entity.get();
    }

    /**
     * UserRepository
     */
    private final UserRepository             userRepository;

    /**
     * AuthenticationSourceRepository
     */
    private final IdentityProviderRepository identityProviderRepository;

    /**
     * UserAuthnBindRepository
     */
    private final UserIdpRepository          userIdpRepository;

    /**
     * ThirdPartyUserRepository
     */
    private final ThirdPartyUserRepository   thirdPartyUserRepository;

    /**
     * AccountConverter
     */
    private final AccountConverter           accountConverter;

}
