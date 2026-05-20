/*
 * ULP - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
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
