/*
 * ulp-application-core - United Login Platform
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
package cn.frank.ulp.application.context;

/**
 * ApplicationContextHolder
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/10/29 22:37
 */
public final class ApplicationContextHolder {
    private static final ThreadLocal<ApplicationContext> HOLDER = new ThreadLocal<>();

    private ApplicationContextHolder() {
    }

    /**
     * Returns the {@link ApplicationContext} bound to the current thread.
     *
     * @return the {@link ApplicationContext}
     */
    public static ApplicationContext getApplicationContext() {
        return HOLDER.get();
    }

    /**
     * Bind the given {@link ApplicationContext} to the current thread.
     *
     * @param applicationContext the {@link ApplicationContext}
     */
    public static void setContext(ApplicationContext applicationContext) {
        if (applicationContext == null) {
            resetContext();
        } else {
            HOLDER.set(applicationContext);
        }
    }

    /**
     * Reset the {@link ApplicationContext} bound to the current thread.
     */
    public static void resetContext() {
        HOLDER.remove();
    }

}
