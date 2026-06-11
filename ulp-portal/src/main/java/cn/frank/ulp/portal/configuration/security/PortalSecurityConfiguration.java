/*
 * ulp-portal - United Login Platform
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
package cn.frank.ulp.portal.configuration.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.RequestCacheConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.fasterxml.jackson.annotation.JsonInclude;

import cn.frank.ulp.audit.event.AuditEventPublish;
import cn.frank.ulp.authentication.alipay.configurer.AlipayAuthenticationConfigurer;
import cn.frank.ulp.authentication.common.IdentityProviderAuthenticationService;
import cn.frank.ulp.authentication.common.client.RegisteredIdentityProviderClientRepository;
import cn.frank.ulp.authentication.common.configurer.IdentityProviderBindAuthenticationConfigurer;
import cn.frank.ulp.authentication.common.jackjson.AuthenticationJacksonModule;
import cn.frank.ulp.authentication.dingtalk.configurer.DingTalkAuthenticationConfigurer;
import cn.frank.ulp.authentication.feishu.configurer.FeiShuAuthenticationConfigurer;
import cn.frank.ulp.authentication.gitee.configurer.GiteeAuthenticationConfigurer;
import cn.frank.ulp.authentication.github.configurer.GithubAuthenticationConfigurer;
import cn.frank.ulp.authentication.otp.mail.configurer.MailOtpAuthenticationConfigurer;
import cn.frank.ulp.authentication.otp.sms.configurer.SmsOtpAuthenticationConfigurer;
import cn.frank.ulp.authentication.qq.configurer.QqAuthenticationConfigurer;
import cn.frank.ulp.authentication.wechat.configurer.WeChatAuthenticationConfigurer;
import cn.frank.ulp.authentication.wechatwork.configurer.WeChatWorkAuthenticationConfigurer;
import cn.frank.ulp.common.repository.account.UserRepository;
import cn.frank.ulp.common.repository.setting.SettingRepository;
import cn.frank.ulp.core.message.mail.MailMsgEventPublish;
import cn.frank.ulp.core.message.sms.SmsMsgEventPublish;
import cn.frank.ulp.core.security.otp.OtpContextHelp;
import cn.frank.ulp.core.security.password.task.PasswordExpireTask;
import cn.frank.ulp.core.security.password.task.impl.PasswordExpireLockTask;
import cn.frank.ulp.core.security.password.task.impl.PasswordExpireWarnTask;
import cn.frank.ulp.core.security.task.UserExpireLockTask;
import cn.frank.ulp.core.security.task.UserUnlockTask;
import cn.frank.ulp.portal.authentication.*;
import cn.frank.ulp.support.geo.GeoLocationParser;
import cn.frank.ulp.support.jackjson.SupportJackson2Module;
import cn.frank.ulp.support.security.authentication.WebAuthenticationDetailsSource;
import cn.frank.ulp.support.security.configurer.FormLoginConfigurer;
import cn.frank.ulp.support.web.useragent.UserAgentParser;

import tools.jackson.databind.ObjectMapper;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.Customizer.withDefaults;

import static cn.frank.ulp.authentication.alipay.configurer.AlipayAuthenticationConfigurer.alipayOauth;
import static cn.frank.ulp.authentication.dingtalk.configurer.DingTalkAuthenticationConfigurer.dingTalkOAuth2;
import static cn.frank.ulp.authentication.feishu.configurer.FeiShuAuthenticationConfigurer.feiShuOAuth2;
import static cn.frank.ulp.authentication.gitee.configurer.GiteeAuthenticationConfigurer.giteeOauth;
import static cn.frank.ulp.authentication.github.configurer.GithubAuthenticationConfigurer.githubOAuth2;
import static cn.frank.ulp.authentication.otp.mail.configurer.MailOtpAuthenticationConfigurer.mailOtp;
import static cn.frank.ulp.authentication.otp.sms.configurer.SmsOtpAuthenticationConfigurer.smsOtp;
import static cn.frank.ulp.authentication.qq.configurer.QqAuthenticationConfigurer.qqOAuth2;
import static cn.frank.ulp.authentication.wechat.configurer.WeChatAuthenticationConfigurer.weChatOauth;
import static cn.frank.ulp.authentication.wechatwork.configurer.WeChatWorkAuthenticationConfigurer.weChatWorkOAuth2;
import static cn.frank.ulp.common.constant.AuthnConstants.LOGIN_CONFIG;
import static cn.frank.ulp.common.constant.ConfigBeanNameConstants.*;
import static cn.frank.ulp.common.constant.SessionConstants.CURRENT_STATUS;
import static cn.frank.ulp.core.security.PublicSecretEndpoint.PUBLIC_SECRET_PATH;
import static cn.frank.ulp.portal.constant.PortalConstants.*;
import static cn.frank.ulp.protocol.code.configurer.AuthenticationUtils.getAuthenticationDetailsSource;
import static cn.frank.ulp.support.constant.UlpConstants.API_PATH;

/**
 * PortalSecurityConfiguration
 *
 * @author Frank Zhang
 */
@EnableMethodSecurity
@Configuration(proxyBeanMethods = false)
public class PortalSecurityConfiguration extends AbstractSecurityConfiguration
                                         implements BeanClassLoaderAware {

    private final AuthenticationFailureHandler failureHandler = new PortalAuthenticationFailureHandler();

    /**
     * webSecurityCustomizer
     *
     * @return {@link WebSecurityCustomizer} WebSecurityCustomizer
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(
            PathPatternRequestMatcher.pathPattern(HttpMethod.GET, "/css/**"),
            PathPatternRequestMatcher.pathPattern(HttpMethod.GET, "/js/**"),
            PathPatternRequestMatcher.pathPattern(HttpMethod.GET, "/webjars/**"),
            PathPatternRequestMatcher.pathPattern(HttpMethod.GET, "/images/**"),
            PathPatternRequestMatcher.pathPattern(HttpMethod.GET, "/favicon.ico"));
    }

    /**
     * IDP SecurityFilterChain
     *
     * @param httpSecurity {@link  HttpSecurity}
     * @return {@link  SecurityFilterChain}
     * @throws Exception Exception
     */
    @RefreshScope
    @Bean(name = IDP_SECURITY_FILTER_CHAIN)
    public SecurityFilterChain idpAuthenticationSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        // @formatter:off
        WebAuthenticationDetailsSource authenticationDetailsSource = getAuthenticationDetailsSource(httpSecurity);
        AuthenticationSuccessHandler successHandler = new PortalAuthenticationSuccessHandler(userRepository,  auditEventPublish );
        List<RequestMatcher> requestMatchers = new ArrayList<>();

        //QQ
        QqAuthenticationConfigurer qq = qqOAuth2(registeredIdentityProviderClientRepository ,identityProviderAuthenticationService)
                .successHandler(successHandler)
                .failureHandler(failureHandler)
                .authenticationDetailsSource(authenticationDetailsSource);
        requestMatchers.add(qq.getRequestMatcher());
        httpSecurity.with(qq,configurer-> {});

        //微信扫码
        WeChatAuthenticationConfigurer chatScanCode = weChatOauth(registeredIdentityProviderClientRepository ,identityProviderAuthenticationService)
                .successHandler(successHandler)
                .failureHandler(failureHandler)
                .authenticationDetailsSource(authenticationDetailsSource);
        requestMatchers.add(chatScanCode.getRequestMatcher());
        httpSecurity.with(chatScanCode,configurer-> {});

        //GITHUB
        GithubAuthenticationConfigurer github = githubOAuth2(registeredIdentityProviderClientRepository ,identityProviderAuthenticationService)
                .successHandler(successHandler)
                .failureHandler(failureHandler)
                .authenticationDetailsSource(authenticationDetailsSource);
        requestMatchers.add(github.getRequestMatcher());
        httpSecurity.with(github,configurer-> {});

        //企业微信
        WeChatWorkAuthenticationConfigurer weChatWork = weChatWorkOAuth2(registeredIdentityProviderClientRepository ,identityProviderAuthenticationService)
                .successHandler(successHandler)
                .failureHandler(failureHandler)
                .authenticationDetailsSource(authenticationDetailsSource);
        requestMatchers.add(weChatWork.getRequestMatcher());
        httpSecurity.with(weChatWork,configurer-> {});

        //钉钉OAuth2
        DingTalkAuthenticationConfigurer dingtalkOauth2 = dingTalkOAuth2(registeredIdentityProviderClientRepository ,identityProviderAuthenticationService)
                .successHandler(successHandler)
                .failureHandler(failureHandler)
                .authenticationDetailsSource(authenticationDetailsSource);
        requestMatchers.add(dingtalkOauth2.getRequestMatcher());
        httpSecurity.with(dingtalkOauth2,configurer-> {});

        //飞书
        FeiShuAuthenticationConfigurer feiShuScanCode = feiShuOAuth2(registeredIdentityProviderClientRepository ,identityProviderAuthenticationService)
                .successHandler(successHandler)
                .failureHandler(failureHandler)
                .authenticationDetailsSource(authenticationDetailsSource);
        requestMatchers.add(feiShuScanCode.getRequestMatcher());
        httpSecurity.with(feiShuScanCode,configurer-> {});


        //Gitee
        GiteeAuthenticationConfigurer giteeCode = giteeOauth(registeredIdentityProviderClientRepository ,identityProviderAuthenticationService)
                .successHandler(successHandler)
                .failureHandler(failureHandler)
                .authenticationDetailsSource(authenticationDetailsSource);
        requestMatchers.add(giteeCode.getRequestMatcher());
        httpSecurity.with(giteeCode,configurer-> {});

        //支付宝
        AlipayAuthenticationConfigurer alipayOauth = alipayOauth(registeredIdentityProviderClientRepository ,identityProviderAuthenticationService)
                .successHandler(successHandler)
                .failureHandler(failureHandler)
                .authenticationDetailsSource(authenticationDetailsSource);
        requestMatchers.add(alipayOauth.getRequestMatcher());
        httpSecurity.with(alipayOauth,configurer-> {});

        //RequestMatcher
        OrRequestMatcher requestMatcher = new OrRequestMatcher(requestMatchers);
        //社交授权请求重定向匹配器
        httpSecurity
            .securityMatcher(requestMatcher)
            .authorizeHttpRequests(registry -> registry.anyRequest().authenticated())
            //安全上下文
            .securityContext(securityContext())
            //异常处理器
            .exceptionHandling(withExceptionConfigurerDefaults())
            //CSRF
            .csrf(withCsrfConfigurerDefaults(requestMatcher))
            //headers
            .headers(withHeadersConfigurerDefaults())
            //cors
            .cors(withCorsConfigurerDefaults())
            //会话管理器
            .sessionManagement(withSessionManagementConfigurerDefaults());
        return httpSecurity.build();
        // @formatter:on
    }

    /**
     * SecurityFilterChain
     *
     * @param httpSecurity {@link  HttpSecurity}
     * @return {@link  SecurityFilterChain}
     * @throws Exception Exception
     */
    @RefreshScope
    @Bean(name = DEFAULT_SECURITY_FILTER_CHAIN)
    @DependsOn({ IDP_SECURITY_FILTER_CHAIN, OIDC_PROTOCOL_SECURITY_FILTER_CHAIN,
                 FORM_PROTOCOL_SECURITY_FILTER_CHAIN, JWT_PROTOCOL_SECURITY_FILTER_CHAIN })
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        // @formatter:off
        WebAuthenticationDetailsSource authenticationDetailsSource = getAuthenticationDetailsSource(httpSecurity);
        AuthenticationSuccessHandler successHandler = new PortalAuthenticationSuccessHandler(userRepository,auditEventPublish);
        // 系统配置
        httpSecurity
                .securityMatcher(API_PATH+"/**")
                //认证请求
                .authorizeHttpRequests(withHttpAuthorizeRequests())
                //安全上下文
                .securityContext(securityContext())
                //请求缓存
                .requestCache(withRequestCacheConfigurer())
                //x509
                .x509(withDefaults())
                //异常处理
                .exceptionHandling(withExceptionConfigurerDefaults())
                //记住我
                .rememberMe(withRememberMeConfigurerDefaults())
                //CSRF
                .csrf(withCsrfConfigurerDefaults())
                //headers
                .headers(withHeadersConfigurerDefaults())
                //cors
                .cors(withCorsConfigurerDefaults())
                //退出配置
                .logout(withLogoutConfigurerDefaults())
                //会话管理器
                .sessionManagement(withSessionManagementConfigurerDefaults())
                .with(withFormLoginConfigurer(),configurer-> {});
        //邮件验证码登录认证
        MailOtpAuthenticationConfigurer mailOtpAuthenticationConfigurer = mailOtp(userRepository, userDetailsService, otpContextHelp)
                .successHandler(successHandler)
                .failureHandler(failureHandler)
                .authenticationDetailsSource(authenticationDetailsSource);
        httpSecurity.with(mailOtpAuthenticationConfigurer,configurer-> {});
        //短信验证码登录认证
        SmsOtpAuthenticationConfigurer smsAuthenticationConfigurer = smsOtp(userRepository, userDetailsService, otpContextHelp)
                .successHandler(successHandler)
                .failureHandler(failureHandler)
                .authenticationDetailsSource(authenticationDetailsSource);
        httpSecurity.with(smsAuthenticationConfigurer,configurer-> {});
        //IDP 绑定用户
        IdentityProviderBindAuthenticationConfigurer identityProviderBindAuthenticationConfigurer = IdentityProviderBindAuthenticationConfigurer.idpBind(identityProviderAuthenticationService, passwordEncoder)
                .successHandler(successHandler)
                .failureHandler(failureHandler)
                .authenticationDetailsSource(authenticationDetailsSource);
        httpSecurity.with(identityProviderBindAuthenticationConfigurer,configurer-> {});
        // @formatter:on
        return httpSecurity.build();
    }

    /**
     * 使用 Http 授权请求
     *
     * @return {@link AuthorizeHttpRequestsConfigurer}
     */
    public Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry> withHttpAuthorizeRequests() {
        //@formatter:off
        return registry -> {
            registry.requestMatchers(PathPatternRequestMatcher.pathPattern(HttpMethod.GET, LOGIN_CONFIG)).permitAll();
            registry.requestMatchers(PathPatternRequestMatcher.pathPattern(HttpMethod.GET, PUBLIC_SECRET_PATH)).permitAll();
            registry.requestMatchers(PathPatternRequestMatcher.pathPattern(HttpMethod.GET, CURRENT_STATUS)).permitAll();
            registry.requestMatchers(PathPatternRequestMatcher.pathPattern(HttpMethod.POST, ACCOUNT_PATH + PREPARE_FORGET_PASSWORD)).permitAll();
            registry.requestMatchers(PathPatternRequestMatcher.pathPattern(HttpMethod.GET, ACCOUNT_PATH + FORGET_PASSWORD_CODE)).permitAll();
            registry.requestMatchers(PathPatternRequestMatcher.pathPattern(HttpMethod.PUT, ACCOUNT_PATH + FORGET_PASSWORD)).permitAll();
            registry.anyRequest().authenticated();
        };
    }



    /**
     * 身份验证成功事件监听器
     *
     * @return {@link  PortalAuthenticationSuccessEventListener}
     */
    @Bean
    @ConditionalOnMissingBean
    public PortalAuthenticationSuccessEventListener authenticationSuccessEventListener() {
        return new PortalAuthenticationSuccessEventListener();
    }

    /**
     * 身份验证失败事件监听器
     *
     * @return {@link  PortalAuthenticationFailureEventListener}
     */
    @Bean
    @ConditionalOnMissingBean
    public PortalAuthenticationFailureEventListener authenticationFailureEventListener() {
        return new PortalAuthenticationFailureEventListener();
    }

    /**
     * 退出成功事件监听器
     *
     * @return {@link  PortalLogoutSuccessEventListener}
     */
    @Bean
    @ConditionalOnMissingBean
    public PortalLogoutSuccessEventListener logoutSuccessEventListener() {
        return new PortalLogoutSuccessEventListener();
    }

    /**
     * 表单登录
     *
     * @return {@link FormLoginConfigurer}
     */
    public FormLoginConfigurer<HttpSecurity> withFormLoginConfigurer() {
        // @formatter:off
        AuthenticationSuccessHandler successHandler = new PortalAuthenticationSuccessHandler(userRepository,  auditEventPublish );
        FormLoginConfigurer<HttpSecurity> configurer=new FormLoginConfigurer<>();
        configurer.successHandler(successHandler)
                .failureHandler(new PortalAuthenticationFailureHandler());
        return configurer;
        // @formatter:on
    }

    /**
     * withRequestCacheConfigurer
     *
     * @return {@link RequestCacheConfigurer}
     */
    public static Customizer<RequestCacheConfigurer<HttpSecurity>> withRequestCacheConfigurer() {
        return configurer -> {
        };
    }

    /**
     * 密码过期锁定任务
     *
     * @param settingRepository {@link  SettingRepository}
     * @param userRepository    {@link  UserRepository}
     * @return {@link  PasswordExpireTask}
     */
    @Bean
    public PasswordExpireTask passwordExpireLockTask(SettingRepository settingRepository,
                                                     UserRepository userRepository) {
        return new PasswordExpireLockTask(settingRepository, userRepository);
    }

    @Bean
    @ConditionalOnMissingBean
    public RedisSerializer<Object> springSessionDefaultRedisSerializer() {
        ObjectMapper mapper = SupportJackson2Module.objectMapperBuilder(this.loader)
            .addModule(new AuthenticationJacksonModule())
            .changeDefaultPropertyInclusion(v -> v.withValueInclusion(JsonInclude.Include.NON_NULL))
            .build();
        return new GenericJacksonJsonRedisSerializer(mapper);
    }

    /**
     * 密码过期警告任务
     *
     * @param settingRepository   {@link  SettingRepository}
     * @param userRepository      {@link  UserRepository}
     * @param mailMsgEventPublish {@link  MailMsgEventPublish}
     * @param smsMsgEventPublish {@link  SmsMsgEventPublish}
     * @return {@link  PasswordExpireTask}
     */
    @Bean
    public PasswordExpireTask passwordExpireWarnTask(SettingRepository settingRepository,
                                                     UserRepository userRepository,
                                                     MailMsgEventPublish mailMsgEventPublish,
                                                     SmsMsgEventPublish smsMsgEventPublish) {
        return new PasswordExpireWarnTask(settingRepository, userRepository, mailMsgEventPublish,
            smsMsgEventPublish);
    }

    /**
     * 密码过期锁定任务
     *
     * @param userRepository    {@link  UserRepository}
     * @return {@link  PasswordExpireTask}
     */
    @Bean
    public UserUnlockTask userUnlockTask(UserRepository userRepository) {
        return new UserUnlockTask(userRepository);
    }

    /**
     * 用户过期锁定任务
     *
     * @param userRepository    {@link  UserRepository}
     * @return {@link  PasswordExpireTask}
     */
    @Bean
    public UserExpireLockTask userExpireLockTask(UserRepository userRepository) {
        return new UserExpireLockTask(userRepository);
    }

    /**
     * WebAuthenticationDetailsSource
     *
     * @param geoLocationParser {@link GeoLocationParser}
     * @return {@link WebAuthenticationDetailsSource}
     */
    @Bean
    public WebAuthenticationDetailsSource authenticationDetailsSource(GeoLocationParser geoLocationParser,
                                                                      UserAgentParser userAgentParser) {
        return new WebAuthenticationDetailsSource(geoLocationParser, userAgentParser);
    }

    /**
     * UserRepository
     */
    private final UserRepository                             userRepository;

    /**
     * UserDetailsService
     */
    private final UserDetailsService                         userDetailsService;

    /**
     * OtpContextHelp
     */
    private final OtpContextHelp                             otpContextHelp;

    /**
     * PasswordEncoder
     */
    private final PasswordEncoder                            passwordEncoder;

    /**
     * AuditEventPublish
     */
    private final AuditEventPublish                          auditEventPublish;

    private final RegisteredIdentityProviderClientRepository registeredIdentityProviderClientRepository;
    private final IdentityProviderAuthenticationService      identityProviderAuthenticationService;

    private ClassLoader                                      loader;

    @Override
    public void setBeanClassLoader(@NonNull ClassLoader classLoader) {
        this.loader = classLoader;
    }

    public PortalSecurityConfiguration(UserAgentParser userAgentParser,
                                       UserRepository userRepository,
                                       UserDetailsService userDetailsService,
                                       OtpContextHelp otpContextHelp,
                                       PasswordEncoder passwordEncoder,
                                       AuditEventPublish auditEventPublish,
                                       SettingRepository settingRepository,
                                       RegisteredIdentityProviderClientRepository registeredIdentityProviderClientRepository,
                                       IdentityProviderAuthenticationService identityProviderAuthenticationService) {
        super(userAgentParser, settingRepository);
        this.userRepository = userRepository;
        this.userDetailsService = userDetailsService;
        this.otpContextHelp = otpContextHelp;
        this.passwordEncoder = passwordEncoder;
        this.auditEventPublish = auditEventPublish;
        this.registeredIdentityProviderClientRepository = registeredIdentityProviderClientRepository;
        this.identityProviderAuthenticationService = identityProviderAuthenticationService;
    }

}
