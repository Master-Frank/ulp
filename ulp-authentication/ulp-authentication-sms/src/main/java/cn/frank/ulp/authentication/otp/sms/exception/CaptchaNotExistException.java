/*
 * eiam-authentication-sms - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.frank.ulp.authentication.otp.sms.exception;

import cn.frank.ulp.support.exception.TopIamException;

/**
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/1/2 23:00
 */
public class CaptchaNotExistException extends TopIamException {
    public CaptchaNotExistException() {
        super("captcha_not_exist", "验证码不存在", DEFAULT_STATUS);
    }
}
