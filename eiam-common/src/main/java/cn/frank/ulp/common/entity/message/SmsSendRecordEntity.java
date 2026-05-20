/*
 * eiam-common - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.frank.ulp.common.entity.message;

import java.time.LocalDateTime;

import org.hibernate.annotations.SoftDelete;

import cn.frank.ulp.common.enums.MessageCategory;
import cn.frank.ulp.common.enums.SmsType;
import cn.frank.ulp.common.message.enums.SmsProvider;
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
 * 短信记录发送表
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/8/1 21:41
 */
@Entity
@Accessors(chain = true)
@Getter
@Setter
@ToString
@Table(name = "eiam_sms_send_record")
@SoftDelete(columnName = IS_DELETED_COLUMN, converter = SoftDeleteConverter.class)
public class SmsSendRecordEntity extends BaseEntity {
    /**
     * phone_
     */
    @Column(name = "phone_")
    private String          phone;

    /**
     * content
     */
    @Column(name = "content_")
    private String          content;

    /**
     * 短信类型
     */
    @Column(name = "type_")
    private SmsType         type;

    /**
     * 消息分类
     */
    @Column(name = "category_")
    private MessageCategory category;

    /**
     * 平台
     */
    @Column(name = "provider_")
    private SmsProvider     provider;

    /**
     * 是否成功
     */
    @Column(name = "is_success")
    private Boolean         success;

    /**
     * 结果
     */
    @Column(name = "result_")
    private String          result;

    /**
     * 发送时间
     */
    @Column(name = "send_time")
    private LocalDateTime   sendTime;
}
