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
package cn.frank.ulp.support.repository;

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

    @Pointcut("@annotation(cn.frank.ulp.support.repository.aspect.query.QuerySingleResult)")
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
