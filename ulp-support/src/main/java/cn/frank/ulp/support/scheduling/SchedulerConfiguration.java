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
package cn.frank.ulp.support.scheduling;

import org.springframework.boot.task.ThreadPoolTaskSchedulerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

/**
 * 调度配置类
 * 用于配置定时任务调度器
 */
@Configuration
@EnableScheduling
public class SchedulerConfiguration implements SchedulingConfigurer {

    /**
    * 线程池任务调度器构建器
    */
    private final ThreadPoolTaskSchedulerBuilder threadPoolTaskSchedulerBuilder;

    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(this.taskScheduler());
    }

    @Bean(name = "taskScheduler", destroyMethod = "shutdown")
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = this.threadPoolTaskSchedulerBuilder.build();
        scheduler.initialize();
        return scheduler;
    }

    /**
    * 构造函数
    *
    * @param threadPoolTaskSchedulerBuilder 线程池任务调度器构建器
    */
    public SchedulerConfiguration(ThreadPoolTaskSchedulerBuilder threadPoolTaskSchedulerBuilder) {
        this.threadPoolTaskSchedulerBuilder = threadPoolTaskSchedulerBuilder;
    }
}
