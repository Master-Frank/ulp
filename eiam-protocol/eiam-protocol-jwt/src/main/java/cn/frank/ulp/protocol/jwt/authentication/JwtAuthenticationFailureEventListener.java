/*
 * eiam-protocol-jwt - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
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
 * @author TopIAM
 * Created by support@topiam.cn on 2023/7/8 21:25
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
