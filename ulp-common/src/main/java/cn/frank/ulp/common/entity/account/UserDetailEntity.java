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
import java.util.Objects;

import org.hibernate.Hibernate;
import org.hibernate.annotations.SoftDelete;

import cn.frank.ulp.common.enums.account.UserIdType;
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
 * 用户详情表
 * </p>
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020-08-07
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@Entity
@Table(name = "eiam_user_detail")
@SoftDelete(columnName = IS_DELETED_COLUMN, converter = SoftDeleteConverter.class)
public class UserDetailEntity extends BaseEntity {

    @Serial
    private static final long serialVersionUID = -3599183663669763315L;
    /**
     * 用户表ID
     */
    @Column(name = "user_id")
    private String            userId;

    /**
     * 证件类型
     */
    @Column(name = "id_type")
    private UserIdType        idType;

    /**
     * 身份证号
     */
    @Column(name = "id_card")
    private String            idCard;

    /**
     * 个人主页
     */
    @Column(name = "website_")
    private String            website;

    /**
     * 地址
     */
    @Column(name = "address_")
    private String            address;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        UserDetailEntity that = (UserDetailEntity) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
