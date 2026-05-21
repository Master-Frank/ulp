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
package cn.frank.ulp.common.entity.message;

import java.time.LocalDateTime;

import org.hibernate.annotations.SoftDelete;

import cn.frank.ulp.common.enums.MailType;
import cn.frank.ulp.common.message.enums.MailProvider;
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
 * 邮件发送记录
 *
 * @author Frank Zhang
 */
@Entity
@Accessors(chain = true)
@Getter
@Setter
@ToString
@Table(name = "ulp_mail_send_record")
@SoftDelete(columnName = IS_DELETED_COLUMN, converter = SoftDeleteConverter.class)
public class MailSendRecordEntity extends BaseEntity {
    /**
     * subject
     */
    @Column(name = "subject_")
    private String        subject;
    /**
     * sender
     */
    @Column(name = "sender_")
    private String        sender;
    /**
     * receiver
     */
    @Column(name = "receiver_")
    private String        receiver;
    /**
     * content
     */
    @Column(name = "content_")
    private String        content;
    /**
     * 消息类型
     */
    @Column(name = "type_")
    private MailType      type;
    /**
     * 平台
     */
    @Column(name = "provider_")
    private MailProvider  provider;

    /**
     * 是否成功
     */
    @Column(name = "is_success")
    private Boolean       success;

    /**
     * 发送时间
     */
    @Column(name = "send_time")
    private LocalDateTime sendTime;
}
