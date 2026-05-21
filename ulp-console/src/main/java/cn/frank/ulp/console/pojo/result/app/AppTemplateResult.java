/*
 * eiam-console - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.frank.ulp.console.pojo.result.app;

import java.io.Serializable;

import cn.frank.ulp.common.enums.app.AppProtocol;
import cn.frank.ulp.common.enums.app.AppType;

import lombok.Data;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 应用模板返回
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/9/27 21:29
 */
@Data
@Schema(description = "应用模板")
public class AppTemplateResult implements Serializable {
    /**
     * code
     */
    @Parameter(description = "CODE")
    private String      code;

    /**
     * protocol
     */
    @Parameter(description = "协议")
    private AppProtocol protocol;

    /**
     * 应用类型
     */
    @Parameter(description = "应用类型")
    private AppType     type;

    /**
     * 名称
     */
    @Parameter(description = "名称")
    private String      name;
    /**
     * 图标
     */
    @Parameter(description = "图标")
    private String      icon;
    /**
     * 描述
     */
    @Parameter(description = "描述")
    private String      desc;
}
