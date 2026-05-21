/*
 * eiam-protocol-jwt - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.frank.ulp.protocol.jwt.exception;

import static cn.frank.ulp.protocol.jwt.constant.JwtProtocolConstants.JWT_ERROR_URI;
import static cn.frank.ulp.protocol.jwt.exception.JwtErrorCodes.GENERATE_ID_TOKEN_ERROR;

/**
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/7/10 21:01
 */
public class IdTokenGenerateException extends JwtAuthenticationException {

    public IdTokenGenerateException() {
        super(new JwtError(GENERATE_ID_TOKEN_ERROR, null, JWT_ERROR_URI));
    }

    public IdTokenGenerateException(Throwable cause) {
        super(new JwtError(GENERATE_ID_TOKEN_ERROR, null, JWT_ERROR_URI), cause);
    }
}
