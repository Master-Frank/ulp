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
package cn.frank.ulp.common.entity.account;

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
 * 用户组表
 * </p>
 *
 * @author Frank Zhang
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@Entity
@Table(name = "ulp_user_group")
@SoftDelete(columnName = IS_DELETED_COLUMN, converter = SoftDeleteConverter.class)
public class UserGroupEntity extends BaseEntity {

    @Serial
    private static final long  serialVersionUID = -2619231849746900857L;

    public static final String CODE_FIELD_NAME  = "code";

    public static final String NAME_FIELD_NAME  = "name";

    /**
     * 用户名
     */
    @Column(name = "name_")
    private String             name;

    /**
     * 密码
     */
    @Column(name = "code_")
    private String             code;
}
