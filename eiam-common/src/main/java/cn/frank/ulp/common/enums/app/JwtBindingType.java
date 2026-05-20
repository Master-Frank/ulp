/*
 * eiam-common - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.frank.ulp.common.enums.app;

import org.springframework.http.HttpMethod;

import com.fasterxml.jackson.annotation.JsonValue;

import cn.frank.ulp.support.enums.BaseEnum;
import cn.frank.ulp.support.web.converter.EnumConvert;

/**
 * Jwt 跳转方式
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/02/15
 */
public enum JwtBindingType implements BaseEnum {
                                                /**
                                                 * redirect
                                                 */
                                                REDIRECT("get", HttpMethod.GET, "重定向跳转"),
                                                /**
                                                 * post
                                                 */
                                                POST("post", HttpMethod.POST, "页面请求跳转");

    /**
     * code
     */
    @JsonValue
    private final String     code;

    /**
     * http method
     */
    private final HttpMethod httpMethod;
    /**
     * desc
     */
    private final String     desc;

    JwtBindingType(String code, HttpMethod httpMethod, String desc) {
        this.code = code;
        this.httpMethod = httpMethod;
        this.desc = desc;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    /**
     * 获取类型
     *
     * @param code {@link String}
     * @return {@link InitLoginType}
     */
    @EnumConvert
    public static JwtBindingType getType(String code) {
        JwtBindingType[] values = values();
        for (JwtBindingType status : values) {
            if (String.valueOf(status.getCode()).equals(code)) {
                return status;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return this.code;
    }
}
