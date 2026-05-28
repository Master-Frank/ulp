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
 * 三方用户表
 *
 * @author Frank Zhang
 */
@Accessors(chain = true)
@Getter
@Setter
@ToString
@Entity
@Table(name = "ulp_third_party_user")
@SoftDelete(columnName = IS_DELETED_COLUMN, converter = SoftDeleteConverter.class)
public class ThirdPartyUserEntity extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 3716649301707341983L;

    /**
     * OpenId
     */
    @Column(name = "open_id")
    private String            openId;

    /**
     * unionId
     */
    @Column(name = "union_id")
    public String             unionId;

    /**
     * 身份提供商 ID
     */
    @Column(name = "idp_id")
    private String            idpId;

    /**
     * 身份提供商 类型
     */
    @Column(name = "idp_type")
    private String            idpType;

    /**
     * 个人邮箱
     */
    @Column(name = "email_")
    private String            email;

    /**
     * 手机号对应的国家号
     */
    @Column(name = "state_code")
    private String            stateCode;

    /**
     * 手机号
     */
    @Column(name = "mobile_")
    private String            mobile;

    /**
     * 昵称
     */
    @Column(name = "nick_name")
    private String            nickName;

    /**
     * 头像url
     */
    @Column(name = "avatar_url")
    private String            avatarUrl;

    /**
     * 附加信息
     */
    @Column(name = "addition_info")
    private String            additionInfo;

    public boolean equals(ThirdPartyUserEntity that) {
        if (this == that) {
            return true;
        }
        if (that == null || getClass() != that.getClass()) {
            return false;
        }
        return Objects.equals(openId, that.openId) && Objects.equals(unionId, that.unionId)
               && Objects.equals(idpId, that.idpId) && Objects.equals(idpType, that.idpType)
               && Objects.equals(email, that.email) && Objects.equals(stateCode, that.stateCode)
               && Objects.equals(mobile, that.mobile) && Objects.equals(nickName, that.nickName)
               && Objects.equals(avatarUrl, that.avatarUrl)
               && Objects.equals(additionInfo, that.additionInfo);
    }
}
