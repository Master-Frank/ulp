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
package cn.frank.ulp.support.lock;

/**
 * 分布式锁异常类
 * 当分布式锁操作出现异常时抛出此异常
 */
public class UlpLockException extends RuntimeException {

    /**
     * 构造函数
     *
     * @param message 异常消息
     * @param cause 异常原因
     */
    public UlpLockException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 默认构造函数
     */
    public UlpLockException() {
        super("获取分布式锁失败");
    }

    /**
     * 构造函数
     *
     * @param message 异常消息
     * @param cause 异常原因
     * @param enableSuppression 是否启用抑制
     * @param writableStackTrace 堆栈跟踪是否可写
     */
    public UlpLockException(String message, Throwable cause, boolean enableSuppression,
                            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    /**
     * 构造函数
     *
     * @param message 异常消息
     */
    public UlpLockException(String message) {
        super(message);
    }

    /**
     * 构造函数
     *
     * @param cause 异常原因
     */
    public UlpLockException(Throwable cause) {
        super(cause);
    }
}
