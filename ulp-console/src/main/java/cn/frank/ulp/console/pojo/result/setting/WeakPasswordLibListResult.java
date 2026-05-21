/*
 * eiam-console - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.frank.ulp.console.pojo.result.setting;

import java.io.Serializable;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 弱密码列表查询结果
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/10/11 21:19
 */
@Data
@RequiredArgsConstructor
@Schema(description = "弱密码列表查询响应")
public class WeakPasswordLibListResult implements Serializable {
    /**
     * value
     */
    @Parameter(description = "value")
    private final String value;
}
