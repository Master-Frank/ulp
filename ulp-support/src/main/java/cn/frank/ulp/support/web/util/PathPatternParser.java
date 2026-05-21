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
package cn.frank.ulp.support.web.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.frank.ulp.support.context.ApplicationContextService;

/**
 * 路径模式解析器类
 * 扩展Spring的PathPatternParser，提供额外的路径参数提取功能
 */
public class PathPatternParser extends org.springframework.web.util.pattern.PathPatternParser {

    /**
    * 提取路径中的参数名
    *
    * @param path 路径
    * @return 参数名列表
    */
    public static List<String> extractParameterNames(String path) {
        String pattern = path;
        List<String> parameterNames = new ArrayList<>();
        Matcher matcher = Pattern
            .compile(
                ApplicationContextService.decryptString("\u00055q\u0015\u0007a\u0004efg\u00053"))
            .matcher(pattern);

        while (matcher.find()) {
            String parameterName = matcher.group(1);
            parameterNames.add(parameterName);
        }

        return parameterNames;
    }
}
