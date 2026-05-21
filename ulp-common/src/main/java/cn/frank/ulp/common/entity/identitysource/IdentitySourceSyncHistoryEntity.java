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

import java.time.LocalDateTime;

import org.hibernate.annotations.SoftDelete;

import cn.frank.ulp.common.enums.SyncStatus;
import cn.frank.ulp.common.enums.TriggerType;
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
 * 身份源同步记录表
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
@Table(name = "eiam_identity_source_sync_history")
@SoftDelete(columnName = IS_DELETED_COLUMN, converter = SoftDeleteConverter.class)
public class IdentitySourceSyncHistoryEntity extends BaseEntity {

    public static final String       IDENTITY_SOURCE_ID_FIELD_NAME = "identitySourceId";

    public static final String       TRIGGER_TYPE_FIELD_NAME       = "triggerType";

    public static final String       OBJECT_TYPE_FIELD_NAME        = "objectType";

    public static final String       STATUS_FIELD_NAME             = "status";

    /**
     * 批号
     */
    @Column(name = "batch_")
    private String                   batch;

    /**
     * 身份源ID
     */
    @Column(name = "identity_source_id")
    private String                   identitySourceId;

    /**
     * 创建数量
     */
    @Column(name = "created_count")
    private Integer                  createdCount;

    /**
     * 更新数量
     */
    @Column(name = "updated_count")
    private Integer                  updatedCount;

    /**
     * 删除数量
     */
    @Column(name = "deleted_count")
    private Integer                  deletedCount;

    /**
     * 跳过数量
     */
    @Column(name = "skipped_count")
    private Integer                  skippedCount;

    /**
     * 开始时间
     */
    @Column(name = "start_time")
    private LocalDateTime            startTime;

    /**
     * 结束时间
     */
    @Column(name = "end_time")
    private LocalDateTime            endTime;

    /**
     * 对象类型（用户、组织）
     */
    @Column(name = "object_type")
    private IdentitySourceObjectType objectType;

    /**
     * 触发类型（手动、任务、事件）
     */
    @Column(name = "trigger_type")
    private TriggerType              triggerType;

    /**
     * 触发类型（手动、任务、事件）
     */
    @Column(name = "status_")
    private SyncStatus               status;
}
