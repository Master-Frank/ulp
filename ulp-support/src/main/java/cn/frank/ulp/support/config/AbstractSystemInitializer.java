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

/**
 * 抽象系统初始化器
 * 提供系统初始化器的基本实现
 */
public abstract class AbstractSystemInitializer implements SystemInitializer {

    /**
     * 获取初始化器的执行顺序
     * 默认返回最小值，确保在其他初始化器之前执行
     *
     * @return 初始化器执行顺序
     */
    @Override
    public int getOrder() {
        return Integer.MIN_VALUE;
    }
}