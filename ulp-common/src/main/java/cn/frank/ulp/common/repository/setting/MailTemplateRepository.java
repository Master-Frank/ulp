/*
 * ulp-common - United Login Platform
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
 * @author Frank Zhang
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
