/*
 * ulp-protocol-jwt - United Login Platform
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
package cn.frank.ulp.protocol.jwt.authentication;

import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.util.Assert;

import com.google.common.collect.Lists;

import cn.frank.ulp.application.jwt.model.JwtProtocolConfig;
import cn.frank.ulp.audit.entity.Target;
import cn.frank.ulp.audit.enums.EventStatus;
import cn.frank.ulp.audit.enums.TargetType;
import cn.frank.ulp.audit.event.AuditEventPublish;
import static cn.frank.ulp.audit.event.type.EventType.APP_SLO;
import static cn.frank.ulp.audit.event.type.EventType.APP_SSO;

/**
 * 认证失败监听
 *
 * @author Frank Zhang
 */
public class JwtAuthenticationFailureEventListener implements
                                                   ApplicationListener<AbstractAuthenticationFailureEvent> {

    /**
     * Handle an application event.
     *
     * @param event the event to respond to
     */
    @Override
    public void onApplicationEvent(@NonNull AbstractAuthenticationFailureEvent event) {
        //登录
        if (event.getAuthentication() instanceof JwtLoginAuthenticationToken authentication) {
            JwtProtocolConfig config = authentication.getConfig();
            auditEventPublish.publish(APP_SSO, authentication, EventStatus.FAIL,
                Lists.newArrayList(Target.builder().id(config.getAppId())
                    .type(TargetType.APPLICATION).name(config.getAppName()).build()));
        }
        //登出
        if (event.getAuthentication() instanceof JwtLogoutAuthenticationToken authentication) {
            JwtProtocolConfig config = authentication.getConfig();
            auditEventPublish.publish(APP_SLO, authentication, EventStatus.FAIL,
                Lists.newArrayList(Target.builder().id(config.getAppId())
                    .type(TargetType.APPLICATION).name(config.getAppName()).build()));
        }
    }

    private final AuditEventPublish auditEventPublish;

    public JwtAuthenticationFailureEventListener(AuditEventPublish auditEventPublish) {
        Assert.notNull(auditEventPublish, "auditEventPublish must not be null ");
        this.auditEventPublish = auditEventPublish;
    }
}
