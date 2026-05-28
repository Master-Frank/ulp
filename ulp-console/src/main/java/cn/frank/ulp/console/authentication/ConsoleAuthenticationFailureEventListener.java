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

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;

import cn.frank.ulp.audit.entity.Actor;
import cn.frank.ulp.audit.enums.EventStatus;
import cn.frank.ulp.audit.event.AuditEventPublish;
import cn.frank.ulp.audit.event.type.EventType;
import cn.frank.ulp.common.entity.setting.AdministratorEntity;
import cn.frank.ulp.common.repository.setting.AdministratorRepository;
import cn.frank.ulp.support.context.ApplicationContextService;
import cn.frank.ulp.support.security.userdetails.UserDetails;
import cn.frank.ulp.support.security.userdetails.UserType;
import static cn.frank.ulp.core.security.util.SecurityUtils.getFailureMessage;

/**
 * 认证失败
 *
 * @author Frank Zhang
 */
public class ConsoleAuthenticationFailureEventListener implements
                                                       ApplicationListener<AbstractAuthenticationFailureEvent> {
    private final Logger logger = LoggerFactory
        .getLogger(ConsoleAuthenticationFailureEventListener.class);

    /**
     * Handle an application event.
     *
     * @param event the event to respond to
     */
    @Override
    public void onApplicationEvent(@NonNull AbstractAuthenticationFailureEvent event) {
        //@formatter:off
        AuditEventPublish publish = ApplicationContextService.getBean(AuditEventPublish.class);
        String content = getFailureMessage(event);
        logger.error("Administrator account authentication failed :{}", event.getException().getMessage());
        String principal = getPrincipal(event);
        if (StringUtils.isNotBlank(principal)){
            Optional<AdministratorEntity> optional = getAdministratorRepository().findByUsername(principal);
            if (optional.isEmpty()) {
                // 手机号
                optional = getAdministratorRepository().findByPhone(principal);
                if (optional.isEmpty()) {
                    // 邮箱
                    optional = getAdministratorRepository().findByEmail(principal);
                }
            }
            if (optional.isEmpty()) {
                logger.error("The administrator account does not exist: [{}]", principal);
                return;
            }
            AdministratorEntity administrator = optional.get();
            Actor actor = Actor.builder().id(administrator.getId()).type(UserType.ADMIN).build();
            publish.publish(EventType.LOGIN_CONSOLE,content+"："+administrator.getUsername(), actor,EventStatus.FAIL);
        }
        //@formatter:on
    }

    private static String getPrincipal(AbstractAuthenticationFailureEvent event) {
        String principal = (String) event.getAuthentication().getPrincipal();
        if (event.getAuthentication().getPrincipal() instanceof String) {
            principal = (String) event.getAuthentication().getPrincipal();
        }
        if (event.getAuthentication().getPrincipal() instanceof UserDetails || event
            .getAuthentication()
            .getPrincipal() instanceof org.springframework.security.core.userdetails.UserDetails) {
            principal = ((UserDetails) event.getAuthentication().getPrincipal()).getUsername();
        }
        return principal;
    }

    private AdministratorRepository getAdministratorRepository() {
        return ApplicationContextService.getBean(AdministratorRepository.class);
    }

}
