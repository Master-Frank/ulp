/*
 * ULP - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.support.repository;

import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;

@Aspect
@Component
public class QuerySingleResultAspect {

    private static final Logger log = LoggerFactory.getLogger(QuerySingleResultAspect.class);

    @Pointcut("@annotation(cn.topiam.employee.support.repository.aspect.query.QuerySingleResult)")
    public void querySingleResultPointcut() {
    }

    @Around("querySingleResultPointcut()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        try {
            Object result = proceedingJoinPoint.proceed();
            if (result instanceof List) {
                List<?> list = (List<?>) result;
                if (list.isEmpty()) {
                    throw new NoResultException("No result found");
                } else if (list.size() > 1) {
                    throw new NonUniqueResultException("Non unique result found");
                } else {
                    return list.get(0);
                }
            }
            return result;
        } catch (Exception e) {
            log.error("Query single result aspect error", e);
            throw e;
        }
    }
}
