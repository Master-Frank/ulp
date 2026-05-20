/*
 * eiam-common - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.frank.ulp.common.message.mail;

import cn.frank.ulp.common.message.enums.MailProvider;

/**
 * None
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/9/27 21:06
 */
public class MailNoneProviderSend implements MailProviderSend {

    @Override
    public void sendMail(SendMailRequest sendMailParam) {

    }

    @Override
    public void sendMailHtml(SendMailRequest sendMailParam) {

    }

    @Override
    public MailProvider getProvider() {
        return null;
    }
}
