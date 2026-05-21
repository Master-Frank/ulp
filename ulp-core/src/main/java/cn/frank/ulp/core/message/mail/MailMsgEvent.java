/*
 * ulp-core - United Login Platform
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
package cn.frank.ulp.core.message.mail;

import java.util.Map;

import org.springframework.context.ApplicationEvent;

import cn.frank.ulp.common.enums.MailType;

import lombok.Getter;

/**
 * 消息事件
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/9/25 21:07
 */
@Getter
public class MailMsgEvent extends ApplicationEvent {
    /**
     * 消息类型
     */
    private final MailType            type;
    /**
     * 接收人
     */
    private final String              receiver;
    /**
     * 参数
     */
    private final Map<String, Object> parameter;

    public MailMsgEvent(MailType type, String receiver, Map<String, Object> parameter) {
        super(type);
        this.type = type;
        this.receiver = receiver;
        this.parameter = parameter;
    }
}
