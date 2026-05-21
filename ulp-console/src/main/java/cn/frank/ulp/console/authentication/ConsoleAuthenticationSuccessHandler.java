/*
 * ulp-console - United Login Platform
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
package cn.frank.ulp.console.authentication;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

import com.google.common.collect.Lists;

import cn.frank.ulp.audit.entity.Target;
import cn.frank.ulp.audit.enums.EventStatus;
import cn.frank.ulp.audit.enums.TargetType;
import cn.frank.ulp.audit.event.AuditEventPublish;
import cn.frank.ulp.authentication.common.IdentityProviderType;
import cn.frank.ulp.authentication.common.authentication.IdentityProviderAuthentication;
import cn.frank.ulp.authentication.common.authentication.OtpAuthentication;
import cn.frank.ulp.common.repository.setting.AdministratorRepository;
import cn.frank.ulp.support.enums.SecretType;
import cn.frank.ulp.support.result.ApiRestResult;
import cn.frank.ulp.support.security.authentication.AuthenticationProvider;
import cn.frank.ulp.support.security.authentication.WebAuthenticationDetails;
import cn.frank.ulp.support.security.constant.SecurityConstants;
import cn.frank.ulp.support.security.password.authentication.NeedChangePasswordAuthenticationToken;
import cn.frank.ulp.support.security.userdetails.UserDetails;
import cn.frank.ulp.support.util.HttpResponseUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import static cn.frank.ulp.audit.event.type.EventType.LOGIN_CONSOLE;
import static cn.frank.ulp.support.constant.EiamConstants.CAPTCHA_CODE_SESSION;
import static cn.frank.ulp.support.security.authentication.AuthenticationProvider.USERNAME_PASSWORD;

/**
 * 认证成功处理程序
 *
 * @author Frank Zhang
 */
public class ConsoleAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    /**
     * Called when a user has been successfully authenticated.
     *
     * @param request        the request which caused the successful authentication
     * @param response       the response
     * @param authentication the <tt>Authentication</tt> object which was created during
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) {
        ApiRestResult<String> result = ApiRestResult.<String> builder().result("success").build();
        request.getSession().removeAttribute(SecretType.LOGIN.getKey());
        request.getSession().removeAttribute(CAPTCHA_CODE_SESSION);
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        //更新 principal
        fillPrincipal(authentication, request, response);
        //更新认证次数
        updateAuthSuccessCount(authentication);
        //记录审计日志
        List<Target> targets = Lists.newArrayList(Target.builder().type(TargetType.CONSOLE)
            .id(principal.getId()).name(principal.getUsername()).build());
        auditEventPublish.publish(LOGIN_CONSOLE, authentication, EventStatus.SUCCESS, targets);

        if (principal.getNeedChangePassword()) {
            SecurityContext securityContext = SecurityContextHolder.getContext();
            securityContext
                .setAuthentication(new NeedChangePasswordAuthenticationToken(authentication));
            securityContextRepository.saveContext(securityContext, request, response);
            HttpResponseUtils.flushResponseJson(response, HttpStatus.OK.value(),
                ApiRestResult.ok().status(SecurityConstants.REQUIRE_RESET_PASSWORD));
            return;
        }

        HttpResponseUtils.flushResponseJson(response, HttpStatus.OK.value(), result);
    }

    private void fillPrincipal(Authentication authentication, HttpServletRequest request,
                               HttpServletResponse response) {
        WebAuthenticationDetails details = (WebAuthenticationDetails) authentication.getDetails();
        //认证类型
        details.setAuthenticationProvider(geAuthType(authentication));
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
        securityContextRepository.saveContext(securityContext, request, response);
    }

    /**
     * 更新认证次数
     *
     * @param authentication {@link Authentication}
     */
    private void updateAuthSuccessCount(Authentication authentication) {
        //认证次数+1
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        WebAuthenticationDetails details = (WebAuthenticationDetails) authentication.getDetails();
        administratorRepository.updateAuthSucceedInfo(principal.getId(),
            details.getGeoLocation().getIp(), details.getAuthenticationTime());
    }

    /**
     * 获取认证类型
     *
     * @param authentication {@link Authentication}
     * @return {@link String}
     */
    public static AuthenticationProvider geAuthType(Authentication authentication) {
        //用户名密码
        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            return USERNAME_PASSWORD;
        }
        //身份提供商
        if (authentication instanceof IdentityProviderAuthentication) {
            IdentityProviderType type = ((IdentityProviderAuthentication) authentication)
                .getProviderType();
            return new AuthenticationProvider(type.value(), type.name());
        }
        //短信/邮箱验证码登录
        if (authentication instanceof OtpAuthentication) {
            String type = ((OtpAuthentication) authentication).getType();
            return new AuthenticationProvider(type, "");
        }
        throw new IllegalArgumentException("未知认证对象");
    }

    private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();
    private final AdministratorRepository   administratorRepository;
    private final AuditEventPublish         auditEventPublish;

    public ConsoleAuthenticationSuccessHandler(AdministratorRepository administratorRepository,
                                               AuditEventPublish auditEventPublish) {
        this.administratorRepository = administratorRepository;
        this.auditEventPublish = auditEventPublish;
    }
}
