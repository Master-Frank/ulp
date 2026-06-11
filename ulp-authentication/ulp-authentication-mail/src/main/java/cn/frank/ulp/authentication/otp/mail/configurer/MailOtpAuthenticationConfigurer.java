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
package cn.frank.ulp.authentication.otp.mail.configurer;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;

import cn.frank.ulp.authentication.otp.mail.filter.MailOtpAuthenticationFilter;
import cn.frank.ulp.authentication.otp.mail.filter.SendMailOtpFilter;
import cn.frank.ulp.common.repository.account.UserRepository;
import cn.frank.ulp.core.security.otp.OtpContextHelp;

import lombok.NonNull;
import lombok.Setter;
import static cn.frank.ulp.support.security.util.HttpSecurityFilterOrderRegistrationUtils.putFilterAfter;

/**
 * 认证配置
 *
 * @author Frank Zhang
 */
public class MailOtpAuthenticationConfigurer extends
                                             AbstractAuthenticationFilterConfigurer<HttpSecurity, MailOtpAuthenticationConfigurer, MailOtpAuthenticationFilter> {

    @Setter
    @NonNull
    private String loginProcessingUrl = MailOtpAuthenticationFilter.DEFAULT_FILTER_PROCESSES_URI;

    @Override
    public void init(HttpSecurity http) {
        //邮箱OTP发送
        http.addFilterBefore(new SendMailOtpFilter(userRepository, otpContextHelp),
            OAuth2LoginAuthenticationFilter.class);
        //邮箱OTP认证
        this.setAuthenticationFilter(
            new MailOtpAuthenticationFilter(userDetailsService, otpContextHelp));
        putFilterAfter(http, this.getAuthenticationFilter(), SendMailOtpFilter.class);

        //登录处理地址
        super.loginProcessingUrl(this.loginProcessingUrl);
        super.init(http);
    }

    public RequestMatcher getRequestMatcher() {
        return MailOtpAuthenticationFilter.getRequestMatcher();
    }

    private final UserRepository     userRepository;
    private final UserDetailsService userDetailsService;

    private final OtpContextHelp     otpContextHelp;

    public MailOtpAuthenticationConfigurer(UserRepository userRepository,
                                           UserDetailsService userDetailsService,
                                           OtpContextHelp otpContextHelp) {
        Assert.notNull(userDetailsService, "userRepository must not be null");
        Assert.notNull(userDetailsService, "userDetailsService must not be null");
        Assert.notNull(otpContextHelp, "otpContextHelp must not be null");
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
        this.otpContextHelp = otpContextHelp;
    }

    /**
     * Create the {@link RequestMatcher} given a loginProcessingUrl
     *
     * @param loginProcessingUrl creates the {@link RequestMatcher} based upon the
     *                           loginProcessingUrl
     * @return the {@link RequestMatcher} to use based upon the loginProcessingUrl
     */
    @Override
    protected RequestMatcher createLoginProcessingUrlMatcher(String loginProcessingUrl) {
        return PathPatternRequestMatcher.pathPattern(HttpMethod.POST, loginProcessingUrl);
    }

    public static MailOtpAuthenticationConfigurer mailOtp(UserRepository userRepository,
                                                          UserDetailsService userDetailsService,
                                                          OtpContextHelp otpContextHelp) {
        return new MailOtpAuthenticationConfigurer(userRepository, userDetailsService,
            otpContextHelp);
    }
}
