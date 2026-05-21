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

import java.util.Objects;

import org.hibernate.Hibernate;
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
 * 组织机构成员
 *
 * @author Frank Zhang
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@Entity
@Table(name = "ulp_organization_member")
@SoftDelete(columnName = IS_DELETED_COLUMN, converter = SoftDeleteConverter.class)
public class OrganizationMemberEntity extends BaseEntity {
    /**
     * 组织机构ID
     */
    @Column(name = "org_id")
    private String orgId;

    /**
     * 用户ID
     */
    @Column(name = "user_id")
    private String userId;

    public OrganizationMemberEntity() {
    }

    public OrganizationMemberEntity(String orgId, String userId) {
        this.orgId = orgId;
        this.userId = userId;
    }

    public OrganizationMemberEntity(String id, String orgId, String userId) {
        super.setId(id);
        this.orgId = orgId;
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        OrganizationMemberEntity that = (OrganizationMemberEntity) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
