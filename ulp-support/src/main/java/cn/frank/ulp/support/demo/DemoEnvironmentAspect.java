/*
 * ULP - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.frank.ulp.support.demo;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class DemoEnvironmentAspect {

    private static final Logger log = LoggerFactory.getLogger(DemoEnvironmentAspect.class);

    @Around("@annotation(cn.frank.ulp.support.demo.Preview)")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        log.warn("Demo environment does not allow this operation");
        throw new DemoEnvDoesNotAllowOperationException();
    }
}
