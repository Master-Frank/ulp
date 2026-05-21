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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import com.google.common.collect.Sets;

import cn.frank.ulp.common.entity.account.UserEntity;
import cn.frank.ulp.common.entity.account.UserGroupMemberEntity;
import cn.frank.ulp.common.entity.account.po.OrganizationPO;
import cn.frank.ulp.common.entity.app.po.AppPO;
import cn.frank.ulp.common.repository.account.OrganizationRepository;
import cn.frank.ulp.common.repository.account.UserGroupMemberRepository;
import cn.frank.ulp.common.repository.account.UserRepository;
import cn.frank.ulp.common.repository.app.AppRepository;
import cn.frank.ulp.portal.service.UserService;
import cn.frank.ulp.support.security.userdetails.Application;
import cn.frank.ulp.support.security.userdetails.Group;
import cn.frank.ulp.support.security.userdetails.Organization;
import cn.frank.ulp.support.security.userdetails.UserDetails;

/**
 * UserService
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2024/3/2 22:22
 */
@Service
public class UserServiceImpl implements UserService {
    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    /**
     * 获取用户详情
     *
     * @param userId {@link String}
     * @return {@link UserDetails}
     */
    @Override
    public UserDetails getUserDetails(String userId) {
        Optional<UserEntity> optional = userRepository.findById(userId);
        return optional.map(this::getUserDetails).orElse(null);
    }

    /**
     * 获取用户详情
     *
     * @param user {@link UserEntity}
     * @return {@link UserEntity}
     */
    @Override
    public UserDetails getUserDetails(UserEntity user) {
        //@formatter:off
        UserDetails details = user.toUserDetails(Sets.newHashSet());
        // 获取用户组信息
        List<UserGroupMemberEntity> userGroupMemberList = userGroupMemberRepository.findByUserId(user.getId());
        details.setGroups(userGroupMemberList.stream().map((group) -> new Group(group.getGroupId())).toList());
        // 获取组织信息
        List<OrganizationPO> organizationList = organizationRepository.getOrganizationList(user.getId());
        details.setOrganizations(organizationList.stream().filter(OrganizationPO::getEnabled).map(org -> new Organization(org.getId(), org.getPath())).toList());
        // 获取用户拥有的应用
        List<String> subjectIds = new ArrayList<>();
        subjectIds.add(user.getId());
        subjectIds.addAll(details.getGroups().stream().map(Group::getId).toList());
        subjectIds.addAll(details.getOrganizations().stream().map(Organization::getId).toList());
        List<AppPO> appList = appRepository.getAppList(subjectIds);
        details.setApplications(appList.stream().map(app -> new Application(app.getId(), app.getCode(), app.getName(),app.getGroup())).toList());
        //@formatter:on
        return details;
    }

    /**
     * 根据用户名、手机号、邮箱查询用户
     *
     * @return {@link UserEntity}
     */
    @Override
    public Optional<UserEntity> findByUsernameOrPhoneOrEmail(String keyword) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        // 异步执行查询
        CompletableFuture<Optional<UserEntity>> findByUsernameFuture = CompletableFuture
            .supplyAsync(() -> userRepository.findByUsername(keyword));

        CompletableFuture<Optional<UserEntity>> findByPhoneFuture = CompletableFuture
            .supplyAsync(() -> userRepository.findByPhone(keyword));

        CompletableFuture<Optional<UserEntity>> findByEmailFuture = CompletableFuture
            .supplyAsync(() -> userRepository.findByEmail(keyword));

        // 等待所有查询完成，并处理结果
        CompletableFuture<Optional<UserEntity>> combinedFuture = CompletableFuture
            .allOf(findByUsernameFuture, findByPhoneFuture, findByEmailFuture).thenApply(voided -> {
                try {
                    if (findByUsernameFuture.get().isPresent()) {
                        return findByUsernameFuture.get();
                    } else if (findByPhoneFuture.get().isPresent()) {
                        return findByPhoneFuture.get();
                    } else {
                        return findByEmailFuture.get();
                    }
                } catch (InterruptedException | ExecutionException e) {
                    return Optional.empty();
                }
            });

        try {
            // 等待最终结果
            return combinedFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            // 处理异常
            return Optional.empty();
        } finally {
            // 结束计时
            stopWatch.stop();
            logger.info("根据用户名、手机号、邮箱查询用户耗时:{}ms", stopWatch.getTotalTimeMillis());
        }
    }

    /**
     * UserRepository
     */
    private final UserRepository            userRepository;

    /**
     * UserGroupMemberRepository
     */
    private final UserGroupMemberRepository userGroupMemberRepository;

    /**
     * OrganizationRepository
     */
    private final OrganizationRepository    organizationRepository;

    /**
     * AppRepository
     */
    private final AppRepository             appRepository;

    public UserServiceImpl(UserRepository userRepository,
                           UserGroupMemberRepository userGroupMemberRepository,
                           OrganizationRepository organizationRepository,
                           AppRepository appRepository) {
        this.userRepository = userRepository;
        this.userGroupMemberRepository = userGroupMemberRepository;
        this.organizationRepository = organizationRepository;
        this.appRepository = appRepository;
    }
}
