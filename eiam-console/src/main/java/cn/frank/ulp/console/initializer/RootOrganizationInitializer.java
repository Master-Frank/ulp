/*
 * eiam-console - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.frank.ulp.console.initializer;

import java.time.LocalDateTime;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

import cn.frank.ulp.common.entity.account.OrganizationEntity;
import cn.frank.ulp.common.enums.account.OrganizationType;
import cn.frank.ulp.common.repository.account.OrganizationRepository;
import cn.frank.ulp.support.config.AbstractSystemInitializer;
import cn.frank.ulp.support.config.InitializationException;
import cn.frank.ulp.support.security.util.SecurityUtils;
import static cn.frank.ulp.support.constant.EiamConstants.*;
import static cn.frank.ulp.support.security.userdetails.DataOrigin.INPUT;

/**
 * SystemInitializer
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2024/04/04 21:24
 */
@Component
public class RootOrganizationInitializer extends AbstractSystemInitializer {

    private final Logger logger = LoggerFactory.getLogger(RootOrganizationInitializer.class);

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void init() throws InitializationException {
        //@formatter:off
        Optional<OrganizationEntity> optional = organizationRepository.findById(ROOT_NODE);
        if (optional.isEmpty()) {
            logger.info("初始化父级组织");
            OrganizationEntity organization = new OrganizationEntity();
            organization.setId(ROOT_NODE);
            organization.setName(ROOT_DEPT_NAME);
            organization.setCode(ROOT_NODE);
            organization.setPath(PATH_SEPARATOR+ROOT_NODE);
            organization.setDisplayPath(PATH_SEPARATOR+ROOT_DEPT_NAME);
            organization.setDataOrigin(INPUT.getType());
            organization.setType(OrganizationType.GROUP);
            organization.setLeaf(false);
            organization.setEnabled(true);
            organization.setOrder(0L);
            organization.setCreateBy(SecurityUtils.getCurrentUserName());
            organization.setCreateTime(LocalDateTime.now());
            organization.setUpdateBy(SecurityUtils.getCurrentUserName());
            organization.setUpdateTime(LocalDateTime.now());
            organization.setRemark("Root organization");
            organizationRepository.batchSave(Lists.newArrayList(organization));
        }
        //@formatter:on
    }

    @Override
    public int getOrder() {
        return 1;
    }

    /**
     * OrganizationRepository
     */
    private final OrganizationRepository organizationRepository;

    /**
     *
     * @param organizationRepository {@link OrganizationRepository}
     */
    public RootOrganizationInitializer(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }
}
