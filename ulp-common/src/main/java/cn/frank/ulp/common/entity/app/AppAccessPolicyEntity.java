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
package cn.frank.ulp.common.entity.app;

import org.hibernate.annotations.SoftDelete;

import cn.frank.ulp.common.enums.app.AppPolicySubjectType;
import cn.frank.ulp.support.repository.SoftDeleteConverter;
import cn.frank.ulp.support.repository.base.BaseEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import static cn.frank.ulp.support.repository.base.BaseEntity.IS_DELETED_COLUMN;

/**
 * 应用授权策略
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/6/4 21:29
 */
@Getter
@Setter
@ToString
@Entity
@Accessors(chain = true)
@Table(name = "eiam_app_access_policy")
@SoftDelete(columnName = IS_DELETED_COLUMN, converter = SoftDeleteConverter.class)
public class AppAccessPolicyEntity extends BaseEntity {
    /**
     * 应用ID
     */
    @Column(name = "app_id")
    private String               appId;

    /**
     * 主体ID（用户、分组、组织机构）
     */
    @Column(name = "subject_id")
    private String               subjectId;

    /**
     * 主体类型（用户、分组、组织机构）
     */
    @Column(name = "subject_type")
    private AppPolicySubjectType subjectType;

    /**
     * 是否启用
     */
    @Column(name = "is_enabled")
    private Boolean              enabled;
}
