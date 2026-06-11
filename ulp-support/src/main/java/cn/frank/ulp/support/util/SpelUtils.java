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
package cn.frank.ulp.support.util;

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
