/*
 * ulp-protocol-form - United Login Platform
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
package cn.frank.ulp.protocol.form.endpoint;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.http.entity.ContentType;
import org.springframework.core.log.LogMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;

import cn.frank.ulp.application.form.model.FormProtocolConfig;
import cn.frank.ulp.common.entity.app.AppFormConfigEntity;
import cn.frank.ulp.protocol.form.authentication.FormAuthenticationToken;
import cn.frank.ulp.protocol.form.endpoint.authentication.FormAuthenticationTokenConverter;
import cn.frank.ulp.protocol.form.endpoint.response.http.converter.FormErrorHttpMessageConverter;
import cn.frank.ulp.protocol.form.exception.FormAuthenticationException;
import cn.frank.ulp.protocol.form.exception.FormError;
import cn.frank.ulp.support.exception.TemplateNotExistException;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import static cn.frank.ulp.protocol.form.constant.FormProtocolConstants.*;
import static cn.frank.ulp.protocol.form.exception.FormErrorCodes.SERVER_ERROR;
import static cn.frank.ulp.support.context.ServletContextService.isHtmlRequest;

/**
 * @author TopIAM
 * Created by support@topiam.cn on 2023/7/8 00:14
 */
public final class FormAuthenticationEndpointFilter extends OncePerRequestFilter {
    /**
     * 认证转换器
     */
    private AuthenticationConverter                            authenticationConverter;

    /**
     * AuthenticationDetailsSource
     */
    private AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource   = new WebAuthenticationDetailsSource();

    /**
     * 认证管理器
     */
    private final AuthenticationManager                        authenticationManager;

    /**
     * 错误响应处理器
     */
    private final HttpMessageConverter<FormError>              errorHttpResponseConverter    = new FormErrorHttpMessageConverter();

    /**
     * 授权端点匹配器
     */
    private final RequestMatcher                               authorizationEndpointMatcher;

    /**
     * 身份验证成功处理程序
     */
    private AuthenticationSuccessHandler                       authenticationSuccessHandler  = this::sendAuthorizationResponse;

    /**
     * 身份验证失败处理程序
     */
    private AuthenticationFailureHandler                       authenticationFailureHandler  = this::sendErrorResponse;

    /**
     * 会话身份策略
     */
    private SessionAuthenticationStrategy                      sessionAuthenticationStrategy = (authentication,
                                                                                                request,
                                                                                                response) -> {
                                                                                             };

    /**
     * Sets the {@link AuthenticationDetailsSource} used for building an authentication details instance from {@link HttpServletRequest}.
     *
     * @param authenticationDetailsSource the {@link AuthenticationDetailsSource} used for building an authentication details instance from {@link HttpServletRequest}
     */
    public void setAuthenticationDetailsSource(AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource) {
        Assert.notNull(authenticationDetailsSource, "authenticationDetailsSource cannot be null");
        this.authenticationDetailsSource = authenticationDetailsSource;
    }

    public void setAuthenticationConverter(AuthenticationConverter authenticationConverter) {
        Assert.notNull(authenticationConverter, "authenticationConverter cannot be null");
        this.authenticationConverter = authenticationConverter;
    }

    public void setAuthenticationSuccessHandler(AuthenticationSuccessHandler authenticationSuccessHandler) {
        Assert.notNull(authenticationSuccessHandler, "authenticationSuccessHandler cannot be null");
        this.authenticationSuccessHandler = authenticationSuccessHandler;
    }

    public void setAuthenticationFailureHandler(AuthenticationFailureHandler authenticationFailureHandler) {
        Assert.notNull(authenticationFailureHandler, "authenticationFailureHandler cannot be null");
        this.authenticationFailureHandler = authenticationFailureHandler;
    }

    public void setSessionAuthenticationStrategy(SessionAuthenticationStrategy sessionAuthenticationStrategy) {
        Assert.notNull(sessionAuthenticationStrategy,
            "sessionAuthenticationStrategy cannot be null");
        this.sessionAuthenticationStrategy = sessionAuthenticationStrategy;
    }

    public FormAuthenticationEndpointFilter(RequestMatcher requestMatcher,
                                            AuthenticationManager authenticationManager) {
        Assert.notNull(authenticationManager, "authenticationManager cannot be null");
        Assert.notNull(requestMatcher, "requestMatcher cannot be empty");
        this.authenticationManager = authenticationManager;
        this.authorizationEndpointMatcher = requestMatcher;
        this.authenticationConverter = new FormAuthenticationTokenConverter();

        configFreemarkerTemplate();
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException,
                                                                      IOException {
        if (!this.authorizationEndpointMatcher.matches(request)) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            Authentication authentication = authenticationConverter.convert(request);

            if (authentication instanceof AbstractAuthenticationToken) {
                ((AbstractAuthenticationToken) authentication)
                    .setDetails(this.authenticationDetailsSource.buildDetails(request));
            }
            //调用认证管理器进行认证
            Authentication authenticationResult = this.authenticationManager
                .authenticate(authentication);

            if (!authenticationResult.isAuthenticated()) {
                // If the Principal (Resource Owner) is not authenticated then
                // pass through the chain with the expectation that the authentication process
                // will commence via AuthenticationEntryPoint
                filterChain.doFilter(request, response);
                return;
            }
            this.sessionAuthenticationStrategy.onAuthentication(authenticationResult, request,
                response);
            this.authenticationSuccessHandler.onAuthenticationSuccess(request, response,
                authenticationResult);
        } catch (FormAuthenticationException ex) {
            if (this.logger.isTraceEnabled()) {
                this.logger.trace(
                    LogMessage.format("Authorization request failed: %s", ex.getError()), ex);
            }
            this.authenticationFailureHandler.onAuthenticationFailure(request, response, ex);
        }
    }

    /**
     * 发送成功响应
     *
     * @param request        {@link HttpServletRequest}
     * @param response       {@link HttpServletResponse}
     * @param authentication {@link Authentication}
     */
    private void sendAuthorizationResponse(HttpServletRequest request, HttpServletResponse response,
                                           Authentication authentication) {
        FormAuthenticationToken authenticationToken = (FormAuthenticationToken) authentication;
        FormProtocolConfig config = authenticationToken.getConfig();
        try {
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setContentType(ContentType.TEXT_HTML.getMimeType());
            Template template = freemarkerTemplateConfiguration.getTemplate("form_redirect.ftlh");
            Map<String, Object> data = new HashMap<>(16);
            data.put(NONCE, System.currentTimeMillis());
            data.put(LOGIN_URL, config.getLoginUrl());
            data.put(SUBMIT_TYPE, config.getSubmitType());
            data.put(USERNAME_FIELD, config.getUsernameField());
            data.put(PASSWORD_FIELD, config.getPasswordField());
            data.put(APP_ACCOUNT_USERNAME, authenticationToken.getAccountUsername());
            data.put(APP_ACCOUNT_PASSWORD, authenticationToken.getAccountCredential());
            List<AppFormConfigEntity.OtherField> otherField = config.getOtherField();
            data.put(OTHER_FIELDS, otherField);
            template.process(data, response.getWriter());
        } catch (TemplateException | IOException e) {
            logger.error("Template processing failed", e);
            FormError error = new FormError(SERVER_ERROR, e.getMessage(), FORM_ERROR_URI);
            throw new FormAuthenticationException(error);
        }
    }

    /**
     * 发送异常响应
     *
     * @param request   {@link HttpServletRequest}
     * @param response  {@link HttpServletResponse}
     * @param exception {@link AuthenticationException}
     */
    private void sendErrorResponse(HttpServletRequest request, HttpServletResponse response,
                                   AuthenticationException exception) throws IOException {
        if (exception instanceof FormAuthenticationException) {
            FormError error = ((FormAuthenticationException) exception).getError();
            boolean isHtmlRequest = isHtmlRequest(request);
            //JSON
            if (!isHtmlRequest) {
                ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);
                errorHttpResponseConverter.write(error, null, httpResponse);
                return;
            }
            //Html
            response.sendError(HttpStatus.BAD_REQUEST.value(), error.toString());
        }
    }

    private void configFreemarkerTemplate() {
        try {
            //模板存放路径
            freemarkerTemplateConfiguration
                .setTemplateLoader(new ClassTemplateLoader(this.getClass(), "/template/"));
            //编码
            freemarkerTemplateConfiguration.setDefaultEncoding(StandardCharsets.UTF_8.name());
            //国际化
            freemarkerTemplateConfiguration.setLocale(new Locale("zh_CN"));
        } catch (Exception exception) {
            throw new TemplateNotExistException(exception);
        }
    }

    /**
     * freemarker 配置实例化
     */
    private final Configuration freemarkerTemplateConfiguration = new Configuration(
        Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
}
