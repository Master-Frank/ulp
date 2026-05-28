/*
 * ulp-authentication-mail - United Login Platform
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
package cn.frank.ulp.authentication.otp.mail.filter;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;

import cn.frank.ulp.authentication.common.IdentityProviderType;
import cn.frank.ulp.authentication.common.authentication.OtpAuthentication;
import cn.frank.ulp.authentication.otp.mail.constant.MailOtpAuthenticationConstants;
import cn.frank.ulp.authentication.otp.mail.exception.CaptchaNotExistException;
import cn.frank.ulp.authentication.otp.mail.exception.MailNotExistException;
import cn.frank.ulp.common.enums.MailType;
import cn.frank.ulp.common.enums.MessageNoticeChannel;
import cn.frank.ulp.core.security.otp.OtpContextHelp;
import cn.frank.ulp.support.exception.InfoValidityFailException;
import cn.frank.ulp.support.result.ApiRestResult;
import cn.frank.ulp.support.util.HttpResponseUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import static cn.frank.ulp.authentication.otp.mail.constant.MailOtpAuthenticationConstants.OTP_LOGIN;
import static cn.frank.ulp.support.exception.enums.ExceptionStatus.EX000102;

/**
 * AbstractOTPAuthenticationFilter
 *
 * @author Frank Zhang
 */
public class MailOtpAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final Logger               logger                       = LoggerFactory
        .getLogger(MailOtpAuthenticationFilter.class);
    /**
     * 请求方法
     */
    public static final String         METHOD                       = "POST";

    private String                     recipientParameter           = MailOtpAuthenticationConstants.RECIPIENT_KEY;
    private String                     codeParameter                = MailOtpAuthenticationConstants.CODE_KEY;

    public final static String         DEFAULT_FILTER_PROCESSES_URI = OTP_LOGIN + "/mail";

    public static final RequestMatcher MAIL_LOGIN_MATCHER           = new AntPathRequestMatcher(
        DEFAULT_FILTER_PROCESSES_URI, HttpMethod.POST.name());

    /**
     * 是否值处理POST请求
     */
    private boolean                    postOnly                     = true;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try {
            if (postOnly && !METHOD.equalsIgnoreCase(request.getMethod())) {
                throw new AuthenticationServiceException(
                    "Authentication method not supported: " + request.getMethod());
            }
            // 获取手机号/邮箱
            String recipient = Objects.toString(obtainUsername(request), "").trim();
            if (StringUtils.isBlank(recipient)) {
                throw new MailNotExistException();
            }
            String code = Objects.toString(obtainCode(request), "").trim();
            if (StringUtils.isBlank(code)) {
                throw new CaptchaNotExistException();
            }
            UserDetails userDetails = userDetailsService.loadUserByUsername(recipient);
            IdentityProviderType type = checkOtp(recipient, code);
            OtpAuthentication authentication = new OtpAuthentication(userDetails, recipient,
                type.value(), userDetails.getAuthorities());
            // Allow subclasses to set the "details" property
            setDetails(request, authentication);
            return authentication;
        } catch (Exception e) {
            HttpResponseUtils.flushResponseJson(response, HttpStatus.BAD_REQUEST.value(),
                ApiRestResult.builder().status(EX000102.getCode()).message(EX000102.getMessage())
                    .build());
            return null;
        }
    }

    public IdentityProviderType checkOtp(String recipient, String code) {
        Boolean checkOtp = otpContextHelp.checkOtp(MailType.LOGIN.getCode(),
            MessageNoticeChannel.MAIL, recipient, code);
        if (!checkOtp) {
            logger.error("用户邮箱: [{}], 验证码: [{}] 认证失败", recipient, code);
            throw new InfoValidityFailException(EX000102.getMessage());
        }
        return IdentityProviderType.MAIL;
    }

    public String getFilterProcessesUri() {
        return DEFAULT_FILTER_PROCESSES_URI;
    }

    /**
     * Enables subclasses to override the composition of the username, such as
     * by including additional values and a separator.
     *
     * @param request so that request attributes can be retrieved
     * @return the username that will be presented in the
     * <code>Authentication</code> request token to the
     * <code>AuthenticationManager</code>
     */
    protected String obtainUsername(HttpServletRequest request) {
        return request.getParameter(recipientParameter);
    }

    protected String obtainCode(HttpServletRequest request) {
        return request.getParameter(codeParameter);
    }

    /**
     * Provided so that subclasses may configure what is put into the
     * authentication request's details property.
     *
     * @param request     that an authentication request is being created for
     * @param authRequest the authentication request object that should have its details
     *                    set
     */
    protected void setDetails(HttpServletRequest request, OtpAuthentication authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }

    /**
     * Defines whether only HTTP POST requests will be allowed by this filter.
     * If set to true, and an authentication request is received which is not a
     * POST request, an exception will be raised immediately and authentication
     * will not be attempted. The <tt>unsuccessfulAuthentication()</tt> method
     * will be called as if handling a failed authentication.
     * <p>
     * Defaults to <tt>true</tt> but may be overridden by subclasses.
     */
    public void setPostOnly(boolean postOnly) {
        this.postOnly = postOnly;
    }

    public final String getRecipientParameter() {
        return recipientParameter;
    }

    public final String getCodeParameter() {
        return codeParameter;
    }

    public void setRecipientParameter(String codeParameter) {
        Assert.hasText(recipientParameter, "Email parameter must not be empty or null");
        this.recipientParameter = codeParameter;
    }

    public void setCodeParameter(String codeParameter) {
        Assert.hasText(codeParameter, "Code parameter must not be empty or null");
        this.codeParameter = codeParameter;
    }

    public static RequestMatcher getRequestMatcher() {
        return MAIL_LOGIN_MATCHER;
    }

    private final OtpContextHelp     otpContextHelp;

    private final UserDetailsService userDetailsService;

    public MailOtpAuthenticationFilter(UserDetailsService userDetailsService,
                                       OtpContextHelp otpContextHelp) {
        super(MAIL_LOGIN_MATCHER);
        this.userDetailsService = userDetailsService;
        this.otpContextHelp = otpContextHelp;
    }
}
