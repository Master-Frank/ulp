/*
 * ulp-support - United Login Platform
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
package cn.frank.ulp.support.trace;

import java.util.UUID;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * 跟踪切面
 * 用于在方法执行前后添加跟踪ID
 */
@Slf4j
@Aspect
@Component
public class TraceAspect {

    /**
    * 跟踪切点
    */
    @Pointcut("@annotation(cn.frank.ulp.support.trace.Trace)")
    public void tracePointcut() {
    }

    /**
    * 环绕通知
    *
    * @param proceedingJoinPoint 连接点
    * @return 方法执行结果
    * @throws Throwable 异常
    */
    @Around("tracePointcut()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        String traceId = UUID.randomUUID().toString();
        TraceUtils.put(traceId);
        try {
            return proceedingJoinPoint.proceed();
        } finally {
            TraceUtils.remove();
        }
    }
}
