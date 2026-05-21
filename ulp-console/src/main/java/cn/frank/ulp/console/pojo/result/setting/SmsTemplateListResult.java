/*
 * eiam-console - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.frank.ulp.console.pojo.result.setting;

import java.io.Serial;
import java.io.Serializable;

import cn.frank.ulp.common.enums.Language;
import cn.frank.ulp.common.enums.MessageCategory;
import cn.frank.ulp.common.enums.SmsType;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 短信配置结果
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/8/17 23:05
 */
@Data
@Schema(description = "获取短信模板列表")
public class SmsTemplateListResult implements Serializable {

    @Serial
    private static final long serialVersionUID = 5983857137670090984L;

    /**
     * 名称
     */
    @Schema(description = "名称")
    private String            name;

    /**
     * 类型
     */
    @Schema(description = "类型")
    private SmsType           type;

    /**
     * 模板类型
     */
    @Schema(description = "模板类型")
    private MessageCategory   category;

    /**
     * 内容
     */
    @Schema(description = "内容")
    private String            content;

    /**
     * Language
     */
    @Schema(description = "Language")
    private Language          language;
}
