/*
 * eiam-protocol-jwt - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.frank.ulp.protocol.jwt.endpoint;

import cn.frank.ulp.protocol.jwt.exception.JwtAuthenticationException;
import cn.frank.ulp.protocol.jwt.exception.JwtError;

/**
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/9/4 13:05
 */
public class JwtAuthenticationEndpointUtils {

    public static void throwError(JwtError jwtError) {
        throw new JwtAuthenticationException(jwtError);
    }
}
