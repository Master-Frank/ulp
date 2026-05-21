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
package cn.frank.ulp.support.config;

import javax.annotation.Nullable;

/**
 * 初始化异常类
 * 用于系统初始化过程中发生的异常
 */
public class InitializationException extends Exception {
    
    /**
     * 序列化版本UID
     */
    private static final long serialVersionUID = 4498093523448059017L;

    /**
     * 构造函数
     */
    public InitializationException() {
    }

    /**
     * 构造函数
     * 
     * @param message 异常信息
     */
    public InitializationException(@Nullable final String message) {
        super(message);
    }

    /**
     * 构造函数
     * 
     * @param message 异常信息
     * @param cause 异常原因
     */
    public InitializationException(@Nullable final String message, @Nullable final Throwable cause) {
        super(message, cause);
    }

    /**
     * 构造函数
     * 
     * @param cause 异常原因
     */
    public InitializationException(@Nullable final Throwable cause) {
        super(cause);
    }
}