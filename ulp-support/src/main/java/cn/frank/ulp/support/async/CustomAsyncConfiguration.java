/*
 * ulp-support - ULP support library (replaces the former eiam-support private jar).
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
package cn.frank.ulp.support.async;

import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.boot.task.ThreadPoolTaskExecutorBuilder;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor;

import com.alibaba.ttl.threadpool.TtlExecutors;

import cn.frank.ulp.support.util.DesensitizationUtils;

/**
 * Custom asynchronous configuration class
 * Used to configure beans related to asynchronous execution
 */
@EnableAsync(
   proxyTargetClass = true
)
public class CustomAsyncConfiguration implements AsyncConfigurer {
   
   /**
    * Thread pool task executor builder
    */
   private final ThreadPoolTaskExecutorBuilder threadPoolTaskExecutorBuilder;
   
   /**
    * Logger
    */
   private final Logger logger;

   /**
    * Get asynchronous uncaught exception handler
    * 
    * @return Asynchronous uncaught exception handler
    */
   public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
      return new SimpleAsyncUncaughtExceptionHandler();
   }

   /**
    * Get asynchronous executor
    * 
    * @return Asynchronous executor
    */
   public Executor getAsyncExecutor() {
      this.logger.debug(DesensitizationUtils.decryptString("qMW^FV\\X\u0012~AF\\\\\u0012kSLY\u001fwGW\\GK]M"));
      ThreadPoolTaskExecutor threadPoolTaskExecutor;
      ThreadPoolTaskExecutor var10000 = threadPoolTaskExecutor = this.threadPoolTaskExecutorBuilder.build();
      var10000.setThreadNamePrefix(DesensitizationUtils.decryptString("\ne\u000ec\u001fgso&O=_*E,\u0007"));
      var10000.initialize();
      return TtlExecutors.getTtlExecutor(
              new ExceptionHandlingAsyncTaskExecutor(
                      new DelegatingSecurityContextAsyncTaskExecutor(threadPoolTaskExecutor)));
   }

   /**
    * Constructor
    * 
    * @param threadPoolTaskExecutorBuilder Thread pool task executor builder
    */
   public CustomAsyncConfiguration(ThreadPoolTaskExecutorBuilder threadPoolTaskExecutorBuilder) {
      this.logger = LoggerFactory.getLogger(CustomAsyncConfiguration.class);
      this.threadPoolTaskExecutorBuilder = threadPoolTaskExecutorBuilder;
   }
}
