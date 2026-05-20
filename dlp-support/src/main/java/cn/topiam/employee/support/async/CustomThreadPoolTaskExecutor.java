/*
 * ULP - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.support.async;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import org.slf4j.MDC;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

public class CustomThreadPoolTaskExecutor extends ThreadPoolTaskExecutor {

    private static final long        serialVersionUID = 1735074699085892566L;

    private Map<String, String>      mdcContextMap;

    private boolean                  useFixedContext;

    private Map<String, String> getMdcContextMap() {
        return this.useFixedContext ? this.mdcContextMap : MDC.getCopyOfContextMap();
    }

    @Override
    public void execute(@NonNull Runnable task) {
        super.execute(wrap(task, this.getMdcContextMap()));
    }

    @Override
    @NonNull
    public Future<?> submit(@NonNull Runnable task) {
        return super.submit(wrap(task, this.getMdcContextMap()));
    }

    @Override
    @NonNull
    public <T> Future<T> submit(@NonNull Callable<T> task) {
        return super.submit(wrap(task, this.getMdcContextMap()));
    }

    private static Runnable wrap(Runnable task, Map<String, String> context) {
        return () -> {
            Map<String, String> previous = MDC.getCopyOfContextMap();
            if (context == null) {
                MDC.clear();
            } else {
                MDC.setContextMap(context);
            }
            try {
                task.run();
            } finally {
                if (previous == null) {
                    MDC.clear();
                } else {
                    MDC.setContextMap(previous);
                }
            }
        };
    }

    private static <T> Callable<T> wrap(Callable<T> task, Map<String, String> context) {
        return () -> {
            Map<String, String> previous = MDC.getCopyOfContextMap();
            if (context == null) {
                MDC.clear();
            } else {
                MDC.setContextMap(context);
            }
            try {
                return task.call();
            } finally {
                if (previous == null) {
                    MDC.clear();
                } else {
                    MDC.setContextMap(previous);
                }
            }
        };
    }

    public CustomThreadPoolTaskExecutor(Map<String, String> mdcContextMap) {
        this.mdcContextMap = mdcContextMap;
        this.useFixedContext = true;
    }

    public CustomThreadPoolTaskExecutor() {
        this.useFixedContext = false;
    }
}
