/*
 * ULP - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.frank.ulp.support.security.web;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn.frank.ulp.support.enums.SecretType;
import cn.frank.ulp.support.exception.UnknownAuthenticationTypeException;
import cn.frank.ulp.support.result.ApiRestResult;
import cn.frank.ulp.support.security.password.PasswordPolicyManager;
import cn.frank.ulp.support.security.password.exception.PasswordInvalidException;
import cn.frank.ulp.support.security.userdetails.UserDetails;
import cn.frank.ulp.support.security.userdetails.UserDetailsService;
import cn.frank.ulp.support.util.AesUtils;
import cn.frank.ulp.support.util.HttpResponseUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationFilter.class);
    private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER = new AntPathRequestMatcher("/login", "POST");
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final UserDetailsService userDetailsService;
    private final PasswordPolicyManager passwordPolicyManager;
    private final SessionRegistry sessionRegistry;
    private final Executor taskExecutor;

    public CustomAuthenticationFilter(UserDetailsService userDetailsService,
                                      PasswordPolicyManager passwordPolicyManager,
                                      SessionRegistry sessionRegistry,
                                      AsyncConfigurer asyncConfigurer) {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER);
        this.userDetailsService = userDetailsService;
        this.passwordPolicyManager = passwordPolicyManager;
        this.sessionRegistry = sessionRegistry;
        this.taskExecutor = asyncConfigurer != null ? asyncConfigurer.getAsyncExecutor() : Runnable::run;
        if (this.taskExecutor == null) {
            logger.debug("No async executor configured");
        }
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
        throws AuthenticationException, IOException, ServletException {
        if (!HttpMethod.POST.name().equals(request.getMethod())) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        Map<String, String> params = readBody(request);
        String username = params.get("username");
        String password = params.get("password");

        String decryptUsername = decryptString(request, username);
        String decryptPassword = decryptString(request, password);

        if (Objects.isNull(decryptUsername)) {
            throw new BadCredentialsException("Username cannot be null");
        }
        if (Objects.isNull(decryptPassword)) {
            throw new BadCredentialsException("Password cannot be null");
        }

        org.springframework.security.core.userdetails.UserDetails loaded = this.userDetailsService
            .loadUserByUsername(decryptUsername);
        if (Objects.isNull(loaded)) {
            throw new BadCredentialsException("Invalid username or password");
        }
        if (!(loaded instanceof UserDetails)) {
            throw new UnknownAuthenticationTypeException();
        }
        UserDetails userDetails = (UserDetails) loaded;

        try {
            this.passwordPolicyManager.validate(userDetails, decryptPassword);
        } catch (PasswordInvalidException e) {
            throw new BadCredentialsException(e.getMessage());
        }

        if (!userDetails.isEnabled()) {
            throw new DisabledException("User account is disabled");
        }
        if (!userDetails.isAccountNonLocked()) {
            throw new LockedException("User account is locked");
        }

        UsernamePasswordAuthenticationToken token = UsernamePasswordAuthenticationToken
            .unauthenticated(userDetails, decryptPassword);
        return getAuthenticationManager().authenticate(token);
    }

    private Map<String, String> readBody(HttpServletRequest request) {
        try {
            String body = request.getReader().lines().reduce("", (a, b) -> a + b);
            if (body.isEmpty()) {
                return Map.of();
            }
            return OBJECT_MAPPER.readValue(body, new TypeReference<Map<String, String>>() {
            });
        } catch (Exception e) {
            logger.error("Failed to parse request parameters", e);
            return Map.of();
        }
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

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult)
        throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
        if (this.sessionRegistry != null) {
            this.sessionRegistry.registerNewSession(request.getSession(true).getId(), authResult.getPrincipal());
        }
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed)
        throws IOException, ServletException {
        String errorMsg = "Invalid username or password";
        if (failed instanceof DisabledException) {
            errorMsg = "User account is disabled";
        } else if (failed instanceof LockedException) {
            errorMsg = "User account is locked";
        }

        ApiRestResult<Object> result = ApiRestResult.builder()
            .status(String.valueOf(HttpStatus.UNAUTHORIZED.value()))
            .message(errorMsg)
            .build();
        HttpResponseUtils.flushResponseJson(response, HttpStatus.UNAUTHORIZED.value(), result);
    }
}
