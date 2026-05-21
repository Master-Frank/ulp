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
package cn.frank.ulp.common.entity.identitysource;

import org.hibernate.annotations.SoftDelete;

import cn.frank.ulp.common.enums.SyncStatus;
import cn.frank.ulp.common.enums.identitysource.IdentitySourceActionType;
import cn.frank.ulp.common.enums.identitysource.IdentitySourceObjectType;
import cn.frank.ulp.support.repository.SoftDeleteConverter;
import cn.frank.ulp.support.repository.base.BaseEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import static cn.frank.ulp.support.repository.base.BaseEntity.IS_DELETED_COLUMN;

/**
 * 身份源同步详情
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/2/22 23:51
 */
@Getter
@Setter
@ToString
@Entity
@Accessors(chain = true)
@NoArgsConstructor
@Table(name = "eiam_identity_source_sync_record")
@SoftDelete(columnName = IS_DELETED_COLUMN, converter = SoftDeleteConverter.class)
public class IdentitySourceSyncRecordEntity extends BaseEntity {

    /**
     * 同步历史ID
     */
    @Column(name = "sync_history_id")
    private String                   syncHistoryId;

    /**
     * 动作类型
     */
    @Column(name = "action_type")
    private IdentitySourceActionType actionType;

    /**
     * 对象ID
     */
    @Column(name = "object_id")
    private String                   objectId;

    /**
     * 对象名称
     */
    @Column(name = "object_name")
    private String                   objectName;

    /**
     * 对象类型
     */
    @Column(name = "object_type")
    private IdentitySourceObjectType objectType;

    /**
     * 状态
     */
    @Column(name = "status_")
    private SyncStatus               status;

    /**
     * 描述
     */
    @Column(name = "desc_")
    private String                   desc;
}
