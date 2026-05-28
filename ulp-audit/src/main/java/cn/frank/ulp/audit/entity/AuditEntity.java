/*
 * ulp-audit - United Login Platform
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
package cn.frank.ulp.audit.entity;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.type.SqlTypes;

import cn.frank.ulp.audit.enums.EventStatus;
import cn.frank.ulp.audit.event.type.EventType;
import cn.frank.ulp.support.repository.SoftDeleteConverter;
import cn.frank.ulp.support.repository.base.BaseEntity;
import cn.frank.ulp.support.security.userdetails.UserType;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import static cn.frank.ulp.support.repository.base.BaseEntity.IS_DELETED_COLUMN;

/**
 * 审计
 *
 * @author Frank Zhang
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = "ulp_audit")
@SoftDelete(columnName = IS_DELETED_COLUMN, converter = SoftDeleteConverter.class)
public class AuditEntity extends BaseEntity {

    @Serial
    private static final long  serialVersionUID      = -3119319193111206582L;

    public static final String EVENT_TYPE_FIELD_NAME = "eventType";

    public static final String ACTOR_ID_FIELD_NAME   = "actorId";

    public static final String EVENT_TIME_FIELD_NAME = "eventTime";
    /**
     * Request Id
     */
    @Column(name = "request_id")
    private String             requestId;

    /**
     * Session Id
     */
    @Column(name = "session_id")
    private String             sessionId;

    /**
     * 操作目标
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "target_")
    private List<Target>       targets;

    /**
     * UserAgent
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "user_agent")
    private UserAgent          userAgent;

    /**
     * 地理位置
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "geo_location")
    private GeoLocation        geoLocation;

    /**
     * 审计事件类型
     */
    @Column(name = "event_type")
    private EventType          eventType;

    /**
     * 参数
     */
    @Column(name = "event_param")
    private String             eventParam;

    /**
     * 事件内容
     */
    @Column(name = "event_content")
    private String             eventContent;

    /**
     * 事件结果
     */
    @Column(name = "event_result")
    private String             eventResult;

    /**
     * 事件时间
     */
    @Column(name = "event_time")
    private LocalDateTime      eventTime;

    /**
     * 事件状态
     */
    @Column(name = "event_status")
    private EventStatus        eventStatus;

    /**
     * 操作者ID
     */
    @Column(name = "actor_id")
    private String             actorId;

    /**
     * 操作人类型
     */
    @Column(name = "actor_type")
    private UserType           actorType;

    /**
     * 身份验证类型
     */
    @Column(name = "actor_auth_type")
    private String             actorAuthType;
}
