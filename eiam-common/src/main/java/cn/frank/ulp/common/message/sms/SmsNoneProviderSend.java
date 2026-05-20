/*
 * eiam-common - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.frank.ulp.common.message.sms;

import cn.frank.ulp.common.message.enums.SmsProvider;

/**
 * None
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/4/20 23:48
 */
public class SmsNoneProviderSend implements SmsProviderSend {
    @Override
    public SmsResponse send(SendSmsRequest request) {
        return null;
    }

    @Override
    public SmsProvider getProvider() {
        return null;
    }
}
