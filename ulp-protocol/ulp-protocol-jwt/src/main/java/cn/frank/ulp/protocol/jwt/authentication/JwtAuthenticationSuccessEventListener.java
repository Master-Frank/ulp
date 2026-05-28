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
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.util.Assert;

import com.google.common.collect.Lists;

import cn.frank.ulp.application.jwt.model.JwtProtocolConfig;
import cn.frank.ulp.audit.entity.Target;
import cn.frank.ulp.audit.enums.EventStatus;
import cn.frank.ulp.audit.enums.TargetType;
import cn.frank.ulp.audit.event.AuditEventPublish;
import static cn.frank.ulp.audit.event.type.EventType.APP_SSO;

/**
 * 监听登录成功事件
 *
 * @author Frank Zhang
 */
public class JwtAuthenticationSuccessEventListener implements
                                                   ApplicationListener<AuthenticationSuccessEvent> {

    /**
     * Handle an application event.
     *
     * @param event the event to respond to
     */
    @Override
    public void onApplicationEvent(@NonNull AuthenticationSuccessEvent event) {
        //@formatter:off
        if (event.getAuthentication() instanceof JwtAuthenticationToken authentication){
            JwtProtocolConfig config = authentication.getConfig();
            auditEventPublish.publish(APP_SSO, (Authentication) authentication.getPrincipal(),EventStatus.SUCCESS,Lists.newArrayList(Target.builder().id(config.getAppId()).type(TargetType.APPLICATION).name(config.getAppName()).build()));
        }
        //@formatter:on
    }

    private final AuditEventPublish auditEventPublish;

    public JwtAuthenticationSuccessEventListener(AuditEventPublish auditEventPublish) {
        Assert.notNull(auditEventPublish, "auditEventPublish must not be null ");
        this.auditEventPublish = auditEventPublish;
    }
}
