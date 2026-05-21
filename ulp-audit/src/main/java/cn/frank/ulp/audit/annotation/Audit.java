/*
 * ulp-audit - United Login Platform
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
package cn.frank.ulp.audit.annotation;

import java.lang.annotation.*;

import cn.frank.ulp.audit.event.type.EventType;

/**
 * Audit
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/9/28 21:56
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Audit {

    /**
     * 类型
     *
     * @return {@link Class}
     */
    EventType type();

    /**
     * 审计内容 支持SPEL表达式
     *
     * @return {@link String}
     */
    String content() default "";
}
