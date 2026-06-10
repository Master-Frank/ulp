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
package cn.frank.ulp.support.config;

import org.springframework.core.Ordered;

/**
 * 初始化器接口
 * 定义系统初始化器的基本行为
 */
public interface Initializer extends Ordered {

    /**
     * 执行初始化操作
     *
     * @throws InitializationException 初始化异常
     */
    void init() throws InitializationException;
}