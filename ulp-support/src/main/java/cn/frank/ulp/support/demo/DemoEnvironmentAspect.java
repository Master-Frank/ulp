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

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Aspect
@Component
@ConditionalOnProperty(prefix = "ulp.demo", name = "enabled", havingValue = "true")
public class DemoEnvironmentAspect {

    private static final Logger log = LoggerFactory.getLogger(DemoEnvironmentAspect.class);

    @Around("@annotation(cn.frank.ulp.support.demo.Preview)")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        log.warn("Demo environment does not allow this operation");
        throw new DemoEnvDoesNotAllowOperationException();
    }
}
