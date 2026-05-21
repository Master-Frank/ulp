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
package cn.frank.ulp.common.entity.setting;

import java.io.Serial;

import org.hibernate.annotations.SoftDelete;

import cn.frank.ulp.common.enums.MailType;
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
 * 邮件模板
 * </p>
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020-08-13
 */
@Getter
@Setter
@ToString
@Entity
@Accessors(chain = true)
@Table(name = "eiam_mail_template")
@SoftDelete(columnName = IS_DELETED_COLUMN, converter = SoftDeleteConverter.class)
public class MailTemplateEntity extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 5983857137670090984L;
    /**
     * 模板类型
     */
    @Column(name = "type_")
    private MailType          type;

    /**
     * 发送人
     * <p>
     * 你可以包括以下宏命令：${client_name}，${time}，${user_email}，${client_description}，${verify_code}。 例如：${client_name} <support@yourcompany.com>
     */
    @Column(name = "sender_")
    private String            sender;

    /**
     * 主题
     * 你可以包括以下宏：${client_name}，${time}，${client_description}，${user_email}。 例如：你正在修改绑定邮箱，你的验证码为：${verify_code}！
     */
    @Column(name = "subject_")
    private String            subject;

    /**
     * 内容
     */
    @Column(name = "content_")
    private String            content;
}
