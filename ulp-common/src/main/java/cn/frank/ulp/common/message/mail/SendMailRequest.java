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

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 发送邮件参数
 *
 * @author Frank Zhang
 */
@Data
@Accessors(chain = true)
public class SendMailRequest {
    /**
     * 发送人
     */
    private String sender;
    /**
     * 收件人
     */
    private String receiver;
    /**
     * 主题
     */
    private String subject;
    /**
     * 内容
     */
    private String body;
}
