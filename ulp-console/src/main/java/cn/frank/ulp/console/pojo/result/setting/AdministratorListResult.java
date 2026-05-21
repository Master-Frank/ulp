/*
 * ulp-console - United Login Platform
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
package cn.frank.ulp.console.pojo.result.setting;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.experimental.Accessors;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import static cn.frank.ulp.support.constant.EiamConstants.DEFAULT_DATE_TIME_FORMATTER_PATTERN;

/**
 * 查询管理员列表结果
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/8/11 23:08
 */
@Data
@Accessors(chain = true)
@Schema(description = "查询管理员列表响应")
public class AdministratorListResult implements Serializable {

    @Serial
    private static final long serialVersionUID = 3320953184046791392L;
    /**
     * ID
     */
    @Parameter(description = "ID")
    private String            id;
    /**
     * 用户名
     */
    @Parameter(description = "用户名")
    private String            username;
    /**
     * 邮箱
     */
    @Parameter(description = "邮箱")
    private String            email;

    /**
     * 手机号
     */
    @Parameter(description = "手机号")
    private String            phone;

    /**
     * 头像URL
     */
    @Parameter(description = "头像URL")
    private String            avatar;

    /**
     * 状态  ENABLE:启用 DISABLE:禁用 LOCKING:锁定
     */
    @Parameter(description = "状态")
    private String            status;

    /**
     * 手机号验证有效
     */
    @Parameter(description = "手机号验证有效")
    private Boolean           phoneVerified;

    /**
     * 邮箱验证有效
     */
    @Parameter(description = "邮箱验证有效")
    private Boolean           emailVerified;

    /**
     * 认证次数
     */
    @Parameter(description = "认证次数")
    private Long              authTotal;

    /**
     * 上次认证IP
     */
    @Parameter(description = "上次认证IP")
    private String            lastAuthIp;
    /**
     * 上次认证时间
     */
    @JsonFormat(pattern = DEFAULT_DATE_TIME_FORMATTER_PATTERN)
    @Parameter(description = "上次认证时间")
    private LocalDateTime     lastAuthTime;

    /**
     * 最后修改时间
     */
    @JsonFormat(pattern = DEFAULT_DATE_TIME_FORMATTER_PATTERN)
    @Parameter(description = "最后修改时间")
    private LocalDateTime     updateTime;

    /**
     * 备注
     */
    @Parameter(description = "备注")
    private String            remark;

    /**
     * 是否为初始化管理员
     */
    @Parameter(description = "是否为初始化管理员")
    private Boolean           initialized;
}
