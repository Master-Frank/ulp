package cn.topiam.employee.support.util;

import java.lang.reflect.Method;
import java.util.Objects;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.StandardReflectionParameterNameDiscoverer;
import org.springframework.expression.spel.standard.SpelExpressionParser;

/**
 * Spring SpEL 表达式工具.
 */
public class SpelUtils {

    private static final SpelExpressionParser DEFAULT_PARSER = new SpelExpressionParser();

    public SpelUtils() {
    }

    public static boolean isSpelExpression(String expression) {
        try {
            new SpelExpressionParser().parseExpression(expression);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String parser(Object root, String spel, Method method, Object[] args) {
        return parser(root, spel, method, args, DEFAULT_PARSER);
    }

    public static String parser(Object root, String spel, Method method, Object[] args,
                                SpelExpressionParser parser) {
        if (!org.springframework.util.StringUtils.hasText(spel)) {
            return "";
        }
        ParameterNameDiscoverer discoverer = new StandardReflectionParameterNameDiscoverer();
        String[] parameterNames = discoverer.getParameterNames(method);
        if (ArrayUtils.isEmpty(parameterNames)) {
            return spel;
        }
        MethodBasedEvaluationContext context = new MethodBasedEvaluationContext(root, method, args,
            discoverer);
        for (int i = 0; i < Objects.requireNonNull(parameterNames).length; i++) {
            context.setVariable(parameterNames[i], args[i]);
        }
        return parser.parseExpression(spel).getValue(context, String.class);
    }
}
