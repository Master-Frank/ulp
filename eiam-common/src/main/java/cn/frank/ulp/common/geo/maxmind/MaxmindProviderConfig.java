/*
 * eiam-common - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.frank.ulp.common.geo.maxmind;

import cn.frank.ulp.common.geo.GeoLocationProviderConfig;
import cn.frank.ulp.common.jackjson.encrypt.JsonPropertyEncrypt;

import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotEmpty;

/**
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/7/23 21:33
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MaxmindProviderConfig extends GeoLocationProviderConfig.GeoLocationConfig {
    /**
     * 密码
     */
    @JsonPropertyEncrypt
    @NotEmpty(message = "密码不能为空")
    private String sessionKey;
}
