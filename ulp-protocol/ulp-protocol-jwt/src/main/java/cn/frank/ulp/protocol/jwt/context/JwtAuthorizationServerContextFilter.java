/*
 * ulp-protocol-jwt - United Login Platform
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
package cn.frank.ulp.protocol.jwt.context;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;

import com.alibaba.fastjson2.JSONObject;

import cn.frank.ulp.application.ApplicationServiceLoader;
import cn.frank.ulp.application.context.ApplicationContext;
import cn.frank.ulp.application.context.ApplicationContextHolder;
import cn.frank.ulp.application.exception.AppNotConfigException;
import cn.frank.ulp.application.exception.AppNotExistException;
import cn.frank.ulp.application.jwt.JwtApplicationService;
import cn.frank.ulp.application.jwt.model.JwtProtocolConfig;
import cn.frank.ulp.common.exception.app.AppAccessDeniedException;
import cn.frank.ulp.protocol.code.EndpointMatcher;
import cn.frank.ulp.support.security.userdetails.Application;
import cn.frank.ulp.support.security.userdetails.UserDetails;
import cn.frank.ulp.support.security.util.SecurityUtils;
import cn.frank.ulp.support.util.IpUtils;

import lombok.Getter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import static cn.frank.ulp.common.constant.ProtocolConstants.APP_CODE;
import static cn.frank.ulp.support.util.HttpRequestUtils.getRequestHeaders;

/**
 * 上下文过滤器
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/6/26 23:55
 */
public final class JwtAuthorizationServerContextFilter extends OncePerRequestFilter {

    public static final String             SEPARATE = "----------------------------------------------------------";

    @Getter
    private final List<EndpointMatcher>    endpointMatchers;

    private final ApplicationServiceLoader applicationServiceLoader;

    public JwtAuthorizationServerContextFilter(List<EndpointMatcher> endpointMatchers,
                                               ApplicationServiceLoader applicationServiceLoader) {
        Assert.notNull(endpointMatchers, "endpointMatchers cannot be null");
        Assert.notNull(applicationServiceLoader, "applicationServiceLoader cannot be null");
        this.applicationServiceLoader = applicationServiceLoader;
        this.endpointMatchers = endpointMatchers;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException,
                                                                      IOException {

        boolean match = false, access = false;
        Map<String, String> variables = new HashMap<>(16);
        for (EndpointMatcher endpointMatcher : endpointMatchers) {
            RequestMatcher requestMatcher = endpointMatcher.getRequestMatcher();
            if (requestMatcher.matches(request)) {
                match = true;
                access = endpointMatcher.getAccess();
                variables = requestMatcher.matcher(request).getVariables();
            }
        }
        if (!match) {
            filterChain.doFilter(request, response);
            return;
        }
        String appCode = variables.get(APP_CODE);
        //校验访问权限（未登录不校验访问权限）
        if (access && SecurityUtils.isAuthenticated()) {
            UserDetails userDetails = SecurityUtils.getCurrentUser();
            Collection<Application> applications = userDetails.getApplications();
            if (applications.stream()
                .noneMatch(application -> application.getCode().equals(appCode))) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN,
                    new AppAccessDeniedException().getMessage());
                return;
            }
        }
        try {
            //@formatter:off
            if (this.logger.isTraceEnabled()) {
                String body = IOUtils.toString(request.getInputStream(),StandardCharsets.UTF_8).replaceAll("\\s+", " ");
                String logs = "\n" +
                        "┣ " + SEPARATE + "\n" +
                        "┣ App: " + appCode + "\n" +
                        "┣ Request url: " + request.getMethod() + " " + request.getRequestURL() + "\n" +
                        "┣ Request ip: " + IpUtils.getIpAddr(request) + "\n" +
                        "┣ Request headers: " + JSONObject.toJSONString(getRequestHeaders(request)) + "\n" +
                        "┣ Request parameters: " + JSONObject.toJSONString(request.getParameterMap()) + "\n" +
                        "┣ Request payload: " + StringUtils.defaultIfBlank(body, "-") + "\n" +
                        "┣ " + SEPARATE;
                logger.trace(logs);
            }
            //查询应用信息封装上下文
            JwtApplicationService applicationService = (JwtApplicationService) applicationServiceLoader.getApplicationServiceByAppCode(appCode);
            JwtProtocolConfig config = applicationService.getProtocolConfig(appCode);
            if (Objects.isNull(config)) {
                throw new AppNotExistException();
            }
            if (!config.getConfigured()){
                throw new AppNotConfigException();
            }
            //设置上下文
            ApplicationContextHolder.setContext(new DefaultApplicationContext(config));
            filterChain.doFilter(request, response);
            //@formatter:on
        } finally {
            ApplicationContextHolder.resetContext();
        }
    }

    private record DefaultApplicationContext(JwtProtocolConfig config) implements ApplicationContext {

        private DefaultApplicationContext {
            Assert.notNull(config, "config cannot be null");
        }

    /**
     * 获取应用ID
     *
     * @return {@link String}
     */
    @Override
    public String getAppId() {
        return this.config.getAppId();
    }

    /**
     * 获取客户端ID
     *
     * @return {@link String}
     */
    @Override
    public String getClientId() {
        return config.getClientId();
    }

    /**
    * 获取应用编码
    *
    * @return {@link String}
    */
    @Override
    public String getAppCode() {
        return config.getAppCode();
    }

    /**
     * 获取应用模版
     *
     * @return {@link String}
     */
    @Override
    public String getAppTemplate() {
        return config.getAppTemplate();
    }

    /**
     * 获取协议配置
     *
     * @return {@link Map}
     */
    @Override
    public Map<String, Object> getConfig() {
        Map<String, Object> config = new HashMap<>(16);
        config.put(JwtProtocolConfig.class.getName(), this.config);
        return config;
    }

}}
