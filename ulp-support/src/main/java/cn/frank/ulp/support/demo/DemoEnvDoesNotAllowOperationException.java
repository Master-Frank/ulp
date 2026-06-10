/*
 * ulp-support - ULP support library
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
package cn.frank.ulp.support.demo;

import org.springframework.http.HttpStatus;

import cn.frank.ulp.support.exception.UlpException;

/**
 * 演示环境不允许操作异常
 * 在演示环境中执行不允许的操作时抛出此异常
 */
public class DemoEnvDoesNotAllowOperationException extends UlpException {
    /**
     * 构造函数
     */
    public DemoEnvDoesNotAllowOperationException() {
        super("演示环境不允许此操作", HttpStatus.FORBIDDEN);
    }
}
