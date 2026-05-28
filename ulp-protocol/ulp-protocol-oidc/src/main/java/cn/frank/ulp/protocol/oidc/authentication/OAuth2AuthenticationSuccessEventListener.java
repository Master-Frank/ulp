/*
 * ulp-protocol-oidc - United Login Platform
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
package cn.frank.ulp.protocol.oidc.authentication;

import java.security.Principal;
import java.util.Objects;

import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.oidc.authentication.OidcLogoutAuthenticationToken;
import org.springframework.util.Assert;

import com.google.common.collect.Lists;

import cn.frank.ulp.audit.entity.Target;
import cn.frank.ulp.audit.enums.EventStatus;
import cn.frank.ulp.audit.enums.TargetType;
import cn.frank.ulp.audit.event.AuditEventPublish;
import cn.frank.ulp.audit.event.type.EventType;
import static cn.frank.ulp.protocol.oidc.constant.OidcProtocolConstants.ID_TOKEN;

/**
 * 监听登录成功事件
 *
 * @author Frank Zhang
 */
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
public class OAuth2AuthenticationSuccessEventListener implements
                                                      ApplicationListener<AuthenticationSuccessEvent> {

    /**
     * Handle an application event.
     *
     * @param event the event to respond to
     */
    @Override
    public void onApplicationEvent(@NonNull AuthenticationSuccessEvent event) {
        //@formatter:off
        if (event.getAuthentication() instanceof OAuth2AccessTokenAuthenticationToken authorization){
            OAuth2Authorization token = authorizationService.findByToken(authorization.getAccessToken().getTokenValue(), OAuth2TokenType.ACCESS_TOKEN);
            if (!Objects.isNull(token) && !Objects.isNull(token.getAttribute(Principal.class.getName()))){
                Authentication principal = token.getAttribute(Principal.class.getName());
                if (!Objects.isNull(principal)) {
                    Target target = Target.builder().id(authorization.getRegisteredClient().getId()).type(TargetType.APPLICATION).name(authorization.getRegisteredClient().getClientName()).build();
                    auditEventPublish.publish(EventType.APP_SSO, principal, EventStatus.SUCCESS, Lists.newArrayList(target));
                }
            }
        }
        if (event.getAuthentication() instanceof OidcLogoutAuthenticationToken authorization) {
            OAuth2Authorization token = authorizationService.findByToken(authorization.getIdTokenHint(), ID_TOKEN);
            if (!Objects.isNull(token) && !Objects.isNull(token.getAttribute(Principal.class.getName()))){
                Authentication principal = token.getAttribute(Principal.class.getName());
                if (!Objects.isNull(principal)) {
                    Target target = Target.builder().id(token.getRegisteredClientId()).name(token.getPrincipalName()).type(TargetType.APPLICATION).build();
                    auditEventPublish.publish(EventType.APP_SLO, principal, EventStatus.SUCCESS, Lists.newArrayList(target));
                }
            }
        }
        //@formatter:on
    }

    private final AuditEventPublish          auditEventPublish;

    private final OAuth2AuthorizationService authorizationService;

    public OAuth2AuthenticationSuccessEventListener(AuditEventPublish auditEventPublish,
                                                    OAuth2AuthorizationService authorizationService) {
        Assert.notNull(auditEventPublish, "auditEventPublish must not be null ");
        Assert.notNull(authorizationService, "authorizationService must not be null ");
        this.auditEventPublish = auditEventPublish;
        this.authorizationService = authorizationService;
    }
}
