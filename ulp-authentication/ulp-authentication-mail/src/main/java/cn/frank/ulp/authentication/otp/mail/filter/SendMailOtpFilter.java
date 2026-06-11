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

import java.io.IOException;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import cn.frank.ulp.authentication.otp.mail.constant.MailOtpAuthenticationConstants;
import cn.frank.ulp.authentication.otp.mail.exception.MailNotExistException;
import cn.frank.ulp.common.entity.account.UserEntity;
import cn.frank.ulp.common.enums.MailType;
import cn.frank.ulp.common.repository.account.UserRepository;
import cn.frank.ulp.core.security.otp.OtpContextHelp;
import cn.frank.ulp.support.result.ApiRestResult;
import cn.frank.ulp.support.util.HttpResponseUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import static cn.frank.ulp.common.enums.MessageNoticeChannel.MAIL;
import static cn.frank.ulp.support.security.constant.SecurityConstants.LOGIN_PATH;

/**
 * 发送OTP
 *
 * @author Frank Zhang
 */
public class SendMailOtpFilter extends OncePerRequestFilter {

    /**
     * 邮件验证码登录路径
     */
    public static final String         LOGIN_MAIL_SEND       = LOGIN_PATH + "/mail/send";

    public static final RequestMatcher MAIL_SEND_OPT_MATCHER = PathPatternRequestMatcher
        .pathPattern(HttpMethod.POST, LOGIN_MAIL_SEND);

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException,
                                                                      IOException {
        if (!getRequestMatcher().matches(request)) {
            filterChain.doFilter(request, response);
            return;
        }
        String recipient = request.getParameter(MailOtpAuthenticationConstants.RECIPIENT_KEY);
        if (StringUtils.isBlank(recipient)) {
            throw new MailNotExistException();
        }
        boolean isSend = sendOtp(recipient);
        //发送OTP
        if (isSend) {
            HttpResponseUtils.flushResponseJson(response, HttpStatus.OK.value(),
                ApiRestResult.ok());
            return;
        }
        HttpResponseUtils.flushResponseJson(response, HttpStatus.OK.value(),
            ApiRestResult.err().message("请输入正确的邮箱"));
    }

    public RequestMatcher getRequestMatcher() {
        return MAIL_SEND_OPT_MATCHER;
    }

    public boolean sendOtp(String recipient) {
        //判断是否存在用户
        Optional<UserEntity> user = userRepository.findByEmail(recipient);
        if (user.isPresent()) {
            otpContextHelp.sendOtp(recipient, MailType.LOGIN.getCode(), MAIL);
            return true;
        }
        return false;
    }

    private final UserRepository userRepository;
    private final OtpContextHelp otpContextHelp;

    public SendMailOtpFilter(UserRepository userRepository, OtpContextHelp otpContextHelp) {
        this.userRepository = userRepository;
        this.otpContextHelp = otpContextHelp;
    }
}
