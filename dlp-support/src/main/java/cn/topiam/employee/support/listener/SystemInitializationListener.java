/*
 * ULP - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.support.listener;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import cn.topiam.employee.support.config.SystemInitializer;

import jakarta.annotation.PostConstruct;

@Component
public class SystemInitializationListener implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger     log = LoggerFactory.getLogger(SystemInitializationListener.class);

    private final List<SystemInitializer> initializers;

    public SystemInitializationListener(List<SystemInitializer> initializers) {
        this.initializers = initializers;
    }

    @Override
    public void onApplicationEvent(@NonNull ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null) {
            this.initializeSystem();
        }
    }

    @PostConstruct
    public void initializeSystem() {
        log.info("System initialization started");
        this.initializers.forEach(initializer -> {
            try {
                initializer.init();
            } catch (Exception e) {
                log.error("System initializer failed", e);
            }
        });
        log.info("System initialization completed");
    }
}
