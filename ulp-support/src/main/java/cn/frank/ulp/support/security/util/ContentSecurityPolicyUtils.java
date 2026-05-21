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
package cn.frank.ulp.support.security.util;

import com.shapesecurity.salvation2.Policy;

/**
 * Content-Security-Policy 解析工具.
 */
public final class ContentSecurityPolicyUtils {

    public ContentSecurityPolicyUtils() {
    }

    /**
     * 解析 CSP 策略字符串。解析过程中若有错误会抛出 {@link ContentSecurityPolicyPolicyException}。
     */
    public static Policy parse(String policy) {
        return Policy.parseSerializedCSP(policy,
            (severity, message, directiveIndex, valueIndex) -> {
                throw new ContentSecurityPolicyPolicyException(
                    new PolicyError(severity, message, directiveIndex, valueIndex));
            });
    }

    public static class ContentSecurityPolicyPolicyException extends RuntimeException {
        private final PolicyError error;

        public ContentSecurityPolicyPolicyException(PolicyError error) {
            this.error = error;
        }

        public PolicyError getError() {
            return error;
        }
    }

    public record PolicyError(Policy.Severity severity, String message, int directiveIndex,
                              int valueIndex) {
        @Override
        public String toString() {
            return "(" + severity.name() + ") " + message + " at directive " + directiveIndex
                   + " at value " + valueIndex;
        }
    }
}
