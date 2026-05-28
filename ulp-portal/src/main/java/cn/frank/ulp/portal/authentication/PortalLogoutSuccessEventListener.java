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
package cn.frank.ulp.portal.authentication;

import java.util.List;

import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.event.LogoutSuccessEvent;

import com.google.common.collect.Lists;

import cn.frank.ulp.audit.entity.Target;
import cn.frank.ulp.audit.enums.EventStatus;
import cn.frank.ulp.audit.enums.TargetType;
import cn.frank.ulp.audit.event.AuditEventPublish;
import cn.frank.ulp.support.context.ApplicationContextService;
import cn.frank.ulp.support.security.userdetails.UserDetails;
import static cn.frank.ulp.audit.event.type.EventType.LOGOUT_PORTAL_PORTAL;

/**
 * 退出成功
 *
 * @author Frank Zhang
 */
public class PortalLogoutSuccessEventListener implements ApplicationListener<LogoutSuccessEvent> {

    /**
     * Handle an application event.
     *
     * @param event the event to respond to
     */
    @Override
    public void onApplicationEvent(@NonNull LogoutSuccessEvent event) {
        AuditEventPublish auditEventPublish = ApplicationContextService
            .getBean(AuditEventPublish.class);
        // 审计事件
        //@formatter:off
        UserDetails principal = (UserDetails) event.getAuthentication().getPrincipal();
        List<Target> targets= Lists.newArrayList(Target.builder().type(TargetType.PORTAL).id(principal.getId()).name(principal.getUsername()).build());
        auditEventPublish.publish(LOGOUT_PORTAL_PORTAL, event.getAuthentication(), EventStatus.SUCCESS,targets);
        //@formatter:on
    }
}
