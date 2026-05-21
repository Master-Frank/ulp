/*
 * eiam-protocol-form - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.frank.ulp.protocol.form.authentication;

import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.util.Assert;

import com.google.common.collect.Lists;

import cn.frank.ulp.application.form.model.FormProtocolConfig;
import cn.frank.ulp.audit.entity.Target;
import cn.frank.ulp.audit.enums.EventStatus;
import cn.frank.ulp.audit.enums.TargetType;
import cn.frank.ulp.audit.event.AuditEventPublish;
import static cn.frank.ulp.audit.event.type.EventType.APP_SSO;

/**
 * 监听登录成功事件
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/9/3
 */
public class FormAuthenticationSuccessEventListener implements
                                                    ApplicationListener<AuthenticationSuccessEvent> {

    /**
     * Handle an application event.
     *
     * @param event the event to respond to
     */
    @Override
    public void onApplicationEvent(@NonNull AuthenticationSuccessEvent event) {
        //@formatter:off
        if (event.getAuthentication() instanceof FormAuthenticationToken authentication){
            FormProtocolConfig config = authentication.getConfig();
            auditEventPublish.publish(APP_SSO, (Authentication) authentication.getPrincipal(), EventStatus.SUCCESS, Lists.newArrayList(Target.builder().id(config.getAppId()).type(TargetType.APPLICATION).name(config.getAppName()).build()));
        }
        //@formatter:on
    }

    private final AuditEventPublish auditEventPublish;

    public FormAuthenticationSuccessEventListener(AuditEventPublish auditEventPublish) {
        Assert.notNull(auditEventPublish, "auditEventPublish must not be null ");
        this.auditEventPublish = auditEventPublish;
    }
}
