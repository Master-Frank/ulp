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
