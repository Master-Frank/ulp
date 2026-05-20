/*
 * eiam-common - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.frank.ulp.common.message.mail;

import cn.frank.ulp.common.message.enums.MailProvider;

/**
 * 邮件收发统一接口
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/9/27 21:06
 */
public interface MailProviderSend {

    /**
     * 发送普通邮件
     *
     * @param sendMailParam 发送邮件的参数
     */
    void sendMail(SendMailRequest sendMailParam);

    /**
     * 发送html的邮件
     *
     * @param sendMailParam 发送邮件的参数
     */
    void sendMailHtml(SendMailRequest sendMailParam);

    /**
     * 服务商类型
     * @return {@link MailProvider}
     */
    MailProvider getProvider();
}
