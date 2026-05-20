/*
 * ULP - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.support.security.web;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import cn.topiam.employee.support.enums.SecretType;
import cn.topiam.employee.support.util.AesUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomLoginFilter extends AbstractAuthenticationProcessingFilter {

    public static final String SECURITY_FORM_USERNAME_KEY = "username";
    public static final String SECURITY_FORM_PASSWORD_KEY = "password";

    private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER = new AntPathRequestMatcher("/login", "POST");

    private String usernameParameter = SECURITY_FORM_USERNAME_KEY;
    private String passwordParameter = SECURITY_FORM_PASSWORD_KEY;
    private boolean postOnly = true;

    public CustomLoginFilter() {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER);
    }

    public CustomLoginFilter(AuthenticationManager authenticationManager) {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER, authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
        throws AuthenticationException {
        if (this.postOnly && !"POST".equals(request.getMethod())) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        String username = obtainUsername(request);
        String password = obtainPassword(request);
        username = username == null ? "" : username.trim();
        password = password == null ? "" : password;

        String decryptUsername = decryptString(request, username);
        String decryptPassword = decryptString(request, password);

        UsernamePasswordAuthenticationToken token = UsernamePasswordAuthenticationToken
            .unauthenticated(decryptUsername, decryptPassword);
        setDetails(request, token);
        return getAuthenticationManager().authenticate(token);
    }

    protected String obtainUsername(HttpServletRequest request) {
        return request.getParameter(this.usernameParameter);
    }

    protected String obtainPassword(HttpServletRequest request) {
        return request.getParameter(this.passwordParameter);
    }

    private String decryptString(HttpServletRequest request, String value) {
        if (value == null || value.isEmpty()) {
            return value;
        }
        try {
            Object secret = request.getSession().getAttribute(SecretType.LOGIN.getKey());
            if (secret instanceof String && !((String) secret).isEmpty()) {
                return AesUtils.decrypt(value, (String) secret);
            }
        } catch (Exception ignored) {
        }
        return value;
    }

    protected void setDetails(HttpServletRequest request, UsernamePasswordAuthenticationToken authRequest) {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }

    public void setUsernameParameter(String usernameParameter) {
        this.usernameParameter = usernameParameter;
    }

    public void setPasswordParameter(String passwordParameter) {
        this.passwordParameter = passwordParameter;
    }

    public void setPostOnly(boolean postOnly) {
        this.postOnly = postOnly;
    }

    public final String getUsernameParameter() {
        return this.usernameParameter;
    }

    public final String getPasswordParameter() {
        return this.passwordParameter;
    }
}
