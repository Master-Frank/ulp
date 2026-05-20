/*
 * eiam-portal - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.frank.ulp.portal.pojo.request;

import java.io.Serial;
import java.io.Serializable;

import cn.frank.ulp.common.enums.MessageNoticeChannel;

import lombok.Data;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 *准备更改密码入参
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/11/13 21:15
 */
@Data
@Schema(description = "准备更改密码入参")
public class PrepareChangePasswordRequest implements Serializable {

    @Serial
    private static final long    serialVersionUID = 5681761697876754485L;

    /**
     * 消息类型
     */
    @NotNull(message = "消息类型不能为空")
    @Parameter(description = "消息类型")
    private MessageNoticeChannel channel;
}
