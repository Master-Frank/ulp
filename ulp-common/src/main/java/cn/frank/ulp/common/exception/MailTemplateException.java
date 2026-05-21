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
package cn.frank.ulp.common.exception;

import java.io.Serial;

import org.springframework.http.HttpStatus;

import cn.frank.ulp.support.exception.TopIamException;

/**
 * 邮件模板异常
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/8/18 21:36
 */
public class MailTemplateException extends TopIamException {
    @Serial
    private static final long serialVersionUID = -6497956209061617684L;

    public MailTemplateException(String msg, Throwable t) {
        super(msg, t);
    }

    public MailTemplateException(String msg) {
        super(msg);
    }

    public MailTemplateException(String msg, HttpStatus status) {
        super(msg, status);
    }

    public MailTemplateException(String error, String description, HttpStatus status) {
        super(error, description, status);
    }

    public MailTemplateException(Throwable cause, String error, String description,
                                 HttpStatus status) {
        super(cause, error, description, status);
    }
}
