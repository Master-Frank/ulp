/*
 * ulp-authentication-sms - United Login Platform
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
package cn.frank.ulp.authentication.otp.sms.filter;

import java.io.IOException;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import cn.frank.ulp.authentication.otp.sms.constant.SmsOtpAuthenticationConstants;
import cn.frank.ulp.authentication.otp.sms.exception.PhoneNotExistException;
import cn.frank.ulp.common.entity.account.UserEntity;
import cn.frank.ulp.common.enums.SmsType;
import cn.frank.ulp.common.repository.account.UserRepository;
import cn.frank.ulp.core.security.otp.OtpContextHelp;
import cn.frank.ulp.support.result.ApiRestResult;
import cn.frank.ulp.support.util.HttpResponseUtils;
import cn.frank.ulp.support.util.PhoneUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import static cn.frank.ulp.common.enums.MessageNoticeChannel.SMS;
import static cn.frank.ulp.support.security.constant.SecurityConstants.LOGIN_PATH;
import static cn.frank.ulp.support.util.PhoneUtils.isPhoneValidate;

/**
 * 发送OTP
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/1/1 22:01
 */
public class SendSmsOtpFilter extends OncePerRequestFilter {
    /**
     * 短信验证码登录路径
     */
    public static final String         LOGIN_SMS_SEND       = LOGIN_PATH + "/sms/send";

    public static final RequestMatcher SMS_SEND_OPT_MATCHER = new AntPathRequestMatcher(
        LOGIN_SMS_SEND, HttpMethod.POST.name());

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException,
                                                                      IOException {
        if (!getRequestMatcher().matches(request)) {
            filterChain.doFilter(request, response);
            return;
        }
        String recipient = request.getParameter(SmsOtpAuthenticationConstants.RECIPIENT_KEY);
        if (StringUtils.isBlank(recipient)) {
            throw new PhoneNotExistException();
        }
        sendOtp(response, recipient);
    }

    public static RequestMatcher getRequestMatcher() {
        return SMS_SEND_OPT_MATCHER;
    }

    public void sendOtp(HttpServletResponse response, String recipient) {
        if (isPhoneValidate(recipient)) {
            //判断是否存在用户
            Optional<UserEntity> user = userRepository
                .findByPhone(PhoneUtils.getPhoneNumber(recipient));
            if (user.isPresent()) {
                otpContextHelp.sendOtp(recipient, SmsType.LOGIN.getCode(), SMS);
                HttpResponseUtils.flushResponseJson(response, HttpStatus.OK.value(),
                    ApiRestResult.ok());
            } else {
                logger.warn("发送验证码登录失败, 手机号不存在: [{" + recipient + "}]");
                HttpResponseUtils.flushResponseJson(response, HttpStatus.OK.value(),
                    ApiRestResult.ok());
            }
        } else {
            HttpResponseUtils.flushResponseJson(response, HttpStatus.OK.value(),
                ApiRestResult.err().message("请输入正确的手机号"));
        }
    }

    private final UserRepository userRepository;
    private final OtpContextHelp otpContextHelp;

    public SendSmsOtpFilter(UserRepository userRepository, OtpContextHelp otpContextHelp) {
        this.userRepository = userRepository;
        this.otpContextHelp = otpContextHelp;
    }
}
