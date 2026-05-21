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
package cn.frank.ulp.common.entity.authn;

import java.io.Serial;

import org.hibernate.annotations.SoftDelete;

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
 * <p>
 * 社交身份认证源配置
 * </p>
 *
 * @author Frank Zhang
 */
@Getter
@Setter
@ToString
@Entity
@Accessors(chain = true)
@Table(name = "ulp_identity_provider")
@SoftDelete(columnName = IS_DELETED_COLUMN, converter = SoftDeleteConverter.class)
public class IdentityProviderEntity extends BaseEntity {

    @Serial
    private static final long  serialVersionUID    = -7936931011805155568L;

    public static final String CATEGORY_FIELD_NAME = "category";

    public static final String NAME_FIELD_NAME     = "name";

    /**
     * 名称
     */
    @Column(name = "name_")
    private String             name;

    /**
     * 唯一CODE 不可修改
     */
    @Column(name = "code_")
    private String             code;

    /**
     * 平台
     */
    @Column(name = "type_")
    private String             type;

    /**
     * 分类
     */
    @Column(name = "category_")
    private String             category;

    /**
     * 配置JSON串
     */
    @Column(name = "config_")
    private String             config;

    /**
     * 是否启用
     */
    @Column(name = "is_enabled")
    private Boolean            enabled;

    /**
     * 是否展示
     */
    @Column(name = "is_displayed")
    private Boolean            displayed;

}
