/*
 * ulp-support - ULP support library (replaces the former eiam-support private jar).
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
package cn.frank.ulp.support.exception;

/**
 * 模板不存在异常
 * <p>
 * 当请求的模板在系统中不存在时抛出此异常，用于标识资源未找到的情况。
 * </p>
 */
public class TemplateNotExistException extends TopIamException {

    /**
     * 构造函数
     *
     * @param cause 导致模板不存在的具体异常原因
     */
    public TemplateNotExistException(Throwable cause) {
        super("模板不存在", cause);
    }
}