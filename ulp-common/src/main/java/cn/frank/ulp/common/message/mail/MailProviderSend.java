/*
 * ulp-common - United Login Platform
 * Copyright (c) 2022-Present Frank Zhang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.frank.ulp.common.message.mail;

import cn.frank.ulp.common.message.enums.MailProvider;

/**
 * 邮件收发统一接口
 *
 * @author Frank Zhang
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
