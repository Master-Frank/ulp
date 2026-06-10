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
package cn.frank.ulp.support.security.savedredirect;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.UrlUtils;

import cn.frank.ulp.support.context.ServletContextService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * HTTP会话重定向缓存实现类
 * 基于HTTP会话实现重定向信息的保存和获取
 */
public class HttpSessionRedirectCache implements RedirectCache {
    /**
    * 日志记录器
    */
    private final Logger logger = LoggerFactory.getLogger(HttpSessionRedirectCache.class);

    /**
    * 获取重定向信息
    *
    * @param request  HTTP请求
    * @param response HTTP响应
    * @return 保存的重定向信息
    */
    @Override
    public SavedRedirect getRedirect(HttpServletRequest request, HttpServletResponse response) {
        return (SavedRedirect) request.getSession().getAttribute("ULP_SECURITY_SAVED_REDIRECT");
    }

    /**
    * 根据参数数组获取参数列表
    *
    * @param parameterMap 参数映射
    * @return 参数列表
    */
    public List<SavedRedirect.Parameter> getParametersByArray(Map<String, String[]> parameterMap) {
        ArrayList<SavedRedirect.Parameter> parameters = new ArrayList<>();

        for (String key : parameterMap.keySet()) {
            SavedRedirect.Parameter parameter = new SavedRedirect.Parameter();
            String[] values = parameterMap.get(key);
            if (values.length == 1) {
                if (!values[0].isEmpty()) {
                    parameter.setKey(key);
                    parameter.setValue(values[0]);
                    parameters.add(parameter);
                }
            }
        }

        return parameters;
    }

    /**
    * 保存重定向信息
    *
    * @param request  HTTP请求
    * @param response HTTP响应
    * @param redirectType 重定向类型
    */
    @Override
    public void saveRedirect(HttpServletRequest request, HttpServletResponse response,
                             RedirectType redirectType) {
        if (redirectType.equals(RedirectType.PARAMETER)
            && StringUtils.isNotBlank(request.getParameter("redirect_uri"))) {
            String redirectUri = request.getParameter("redirect_uri");
            SavedRedirect savedRedirect = new SavedRedirect();
            int queryIndex = redirectUri.indexOf("?");
            if (queryIndex > -1) {
                savedRedirect.setAction(redirectUri.substring(0, queryIndex));
            } else {
                savedRedirect.setAction(redirectUri);
            }

            savedRedirect.setParameters(
                getParameters(ServletContextService.getParameterForUrl(redirectUri)));
            savedRedirect.setMethod(HttpMethod.GET.name());
            request.getSession().setAttribute("ULP_SECURITY_SAVED_REDIRECT", savedRedirect);
            this.logger.debug("保存参数类型重定向信息");
        }

        if (redirectType.equals(RedirectType.REQUEST)) {
            SavedRedirect savedRedirect = new SavedRedirect();
            savedRedirect.setParameters(this.getParametersByArray(request.getParameterMap()));
            savedRedirect.setMethod(request.getMethod());
            savedRedirect.setAction(UrlUtils.buildFullRequestUrl(request.getScheme(),
                request.getServerName(), request.getServerPort(), request.getRequestURI(), null));
            request.getSession().setAttribute("ULP_SECURITY_SAVED_REDIRECT", savedRedirect);
            this.logger.debug("保存请求类型重定向信息");
        }
    }

    /**
    * 移除重定向信息
    *
    * @param request  HTTP请求
    * @param response HTTP响应
    */
    @Override
    public void removeRedirect(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().removeAttribute("ULP_SECURITY_SAVED_REDIRECT");
        this.logger.debug("移除重定向信息");
    }

    /**
    * 根据参数映射获取参数列表
    *
    * @param parameterMap 参数映射
    * @return 参数列表
    */
    public static List<SavedRedirect.Parameter> getParameters(Map<String, String> parameterMap) {
        ArrayList<SavedRedirect.Parameter> parameters = new ArrayList<>();
        Iterator<String> iterator = parameterMap.keySet().iterator();

        while (iterator.hasNext()) {
            String key = iterator.next();
            SavedRedirect.Parameter parameter = new SavedRedirect.Parameter();
            parameter.setKey(key);
            parameter.setValue(parameterMap.get(key));
            parameters.add(parameter);
        }

        return parameters;
    }
}
