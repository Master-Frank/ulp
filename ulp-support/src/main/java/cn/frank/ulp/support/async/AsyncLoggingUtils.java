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

import java.util.Map;
import java.util.function.Supplier;

import org.slf4j.MDC;

/**
 * Asynchronous logging utility class
 * Used to pass MDC context information in asynchronous tasks
 */
public class AsyncLoggingUtils {

    /**
     * Wrap Supplier to pass MDC context
     *
     * @param supplier Original Supplier
     * @return Wrapped Supplier that can be used with MDC context in asynchronous tasks
     * @param <U> Return value type
     */
    public static <U> Supplier<U> withMdc(Supplier<U> supplier) {
        // Get the MDC context of the current thread
        Map<String, String> contextMap = MDC.getCopyOfContextMap();
        return () -> {
            // Set MDC context in asynchronous tasks
            if (contextMap != null) {
                MDC.setContextMap(contextMap);
            }
            // Execute the original Supplier and return the result
            return supplier.get();
        };
    }

    /**
     * Private constructor to prevent instantiation
     */
    private AsyncLoggingUtils() {
    }
}
