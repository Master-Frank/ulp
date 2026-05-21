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
 * 应用账户
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/5/31 21:51
 */
@Getter
@Setter
@ToString
@Entity
@Accessors(chain = true)
@Table(name = "eiam_app_account")
@SoftDelete(columnName = IS_DELETED_COLUMN, converter = SoftDeleteConverter.class)
public class AppAccountEntity extends BaseEntity {
    /**
     * 应用ID
     */
    @Column(name = "app_id")
    private String  appId;

    /**
     * 用户ID
     */
    @Column(name = "user_id")
    private String  userId;

    /**
     * 账户名称
     */
    @Column(name = "account_")
    private String  account;

    /**
     * 账户密码
     */
    @Column(name = "password_")
    private String  password;

    /**
     * 默认的
     */
    @Column(name = "default_")
    private Boolean defaulted;
}
