/*
 * eiam-protocol-core - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.frank.ulp.protocol.code;

import java.io.Serializable;

import org.springframework.security.web.util.matcher.RequestMatcher;

import lombok.Data;

/**
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2024/2/27 09:43
 */
@Data
public class EndpointMatcher implements Serializable {

    /**
     * 请求匹配器
     */
    private RequestMatcher requestMatcher;

    /**
     * access
     */
    private Boolean        access;

    public EndpointMatcher(RequestMatcher requestMatcher, Boolean access) {
        this.requestMatcher = requestMatcher;
        this.access = access;
    }

    public EndpointMatcher() {
    }
}
