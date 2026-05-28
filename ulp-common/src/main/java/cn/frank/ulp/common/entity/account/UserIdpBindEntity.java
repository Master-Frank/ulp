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
import java.time.LocalDateTime;

import org.hibernate.annotations.SoftDelete;

import cn.frank.ulp.support.repository.SoftDeleteConverter;
import cn.frank.ulp.support.repository.base.BaseEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import jakarta.persistence.*;
import static cn.frank.ulp.support.repository.base.BaseEntity.IS_DELETED_COLUMN;

/**
 * 用户认证方式绑定表
 *
 * @author Frank Zhang
 */
@Accessors(chain = true)
@Getter
@Setter
@ToString
@Entity
@Table(name = "ulp_user_idp_bind")
@SoftDelete(columnName = IS_DELETED_COLUMN, converter = SoftDeleteConverter.class)
public class UserIdpBindEntity extends BaseEntity {

    @Serial
    private static final long    serialVersionUID = -14364708756807242L;

    /**
     * 用户ID
     */
    @Column(name = "user_id")
    private String               userId;

    /**
     * 三方用户表ID
     */
    @ManyToOne
    @JoinColumn(name = "third_party_user_id")
    @ToString.Exclude
    private ThirdPartyUserEntity thirdPartyUser;

    /**
     * 绑定时间
     */
    @Column(name = "bind_time")
    private LocalDateTime        bindTime;
}
