/*
 * eiam-common - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.frank.ulp.common.repository.setting;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn.frank.ulp.common.entity.setting.MailTemplateEntity;
import cn.frank.ulp.common.enums.MailType;

/**
 * <p>
 * 邮件模板 Repository 接口
 * </p>
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020-08-13
 */
@Repository
public interface MailTemplateRepository extends JpaRepository<MailTemplateEntity, Long> {
    /**
     * 根据类型查询模板
     *
     * @param type {@link MailType}
     * @return {@link MailTemplateEntity}
     */
    MailTemplateEntity findByType(@Param("type") MailType type);

    /**
     * 根据模块类型删除模块
     *
     * @param type {@link MailType}
     */
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    void deleteByType(@Param("type") MailType type);
}
