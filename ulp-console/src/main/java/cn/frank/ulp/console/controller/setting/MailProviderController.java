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
package cn.frank.ulp.console.controller.setting;

import java.util.HashMap;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import cn.frank.ulp.audit.annotation.Audit;
import cn.frank.ulp.audit.event.type.EventType;
import cn.frank.ulp.common.enums.MailType;
import cn.frank.ulp.console.pojo.result.setting.EmailProviderConfigResult;
import cn.frank.ulp.console.pojo.save.setting.MailProviderSaveParam;
import cn.frank.ulp.console.service.setting.MessageSettingService;
import cn.frank.ulp.core.context.ContextService;
import cn.frank.ulp.core.message.MsgVariable;
import cn.frank.ulp.core.message.mail.MailMsgEventPublish;
import cn.frank.ulp.support.demo.Preview;
import cn.frank.ulp.support.lock.Lock;
import cn.frank.ulp.support.result.ApiRestResult;

import lombok.AllArgsConstructor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import static cn.frank.ulp.common.constant.SettingConstants.SETTING_PATH;

/**
 * 消息设置
 *
 * @author Frank Zhang
 */
@Validated
@Tag(name = "邮件提供商")
@RestController
@AllArgsConstructor
@RequestMapping(value = SETTING_PATH + "/message/mail_provider")
public class MailProviderController {

    /**
     * 保存邮件服务商配置
     *
     * @param param {@link MailProviderSaveParam}
     * @return {@link ApiRestResult}
     */
    @Lock
    @Preview
    @Validated
    @Operation(summary = "保存邮件服务提供商配置")
    @Audit(type = EventType.SAVE_MAIL_SERVICE)
    @PostMapping("save")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(cn.frank.ulp.support.security.userdetails.UserType).ADMIN)")
    public ApiRestResult<Boolean> saveEmailProviderConfig(@RequestBody MailProviderSaveParam param) {
        Boolean result = messageSettingService.saveMailProviderConfig(param);
        return ApiRestResult.ok(result);
    }

    /**
     * 获取邮件服务商配置
     *
     * @return {@link ApiRestResult}
     */
    @Operation(summary = "获取邮件服务提供商配置")
    @GetMapping("config")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(cn.frank.ulp.support.security.userdetails.UserType).ADMIN)")
    public ApiRestResult<EmailProviderConfigResult> getEmailProviderConfig() {
        return ApiRestResult.ok(messageSettingService.getMailProviderConfig());
    }

    /**
     * 禁用邮件提供商
     *
     * @return {@link ApiRestResult}
     */
    @Lock
    @Preview
    @Validated
    @Operation(summary = "禁用邮件服务提供商")
    @Audit(type = EventType.OFF_MAIL_SERVICE)
    @PutMapping(value = "/disable")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(cn.frank.ulp.support.security.userdetails.UserType).ADMIN)")
    public ApiRestResult<Boolean> disableMailProvider() {
        Boolean result = messageSettingService.disableMailProvider();
        return ApiRestResult.ok(result);
    }

    /**
     * 邮件发送测试
     *
     * @param mailType {@link MailType}
     * @param receiver {@link String}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "发送测试邮件")
    @GetMapping(value = "/test")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(cn.frank.ulp.support.security.userdetails.UserType).ADMIN)")
    public ApiRestResult<Boolean> sendMail(MailType mailType, String receiver) {
        HashMap<String, Object> map = new HashMap<>(16);
        if (mailType == MailType.UPDATE_PASSWORD || mailType == MailType.UPDATE_BIND_MAIL
            || mailType == MailType.RESET_PASSWORD) {
            map.put(MsgVariable.VERIFY_CODE, RandomStringUtils.randomAlphanumeric(6));
        }
        map.put(MsgVariable.TEST, "(TEST)");
        map.put(MsgVariable.EXPIRE_DAYS, "3");
        map.put("verify_link", ContextService.getPortalPublicBaseUrl());
        mailMsgEventPublish.publish(mailType, receiver, map);
        return ApiRestResult.ok();
    }

    /**
     * MailMsgEventPublish
     */
    private final MailMsgEventPublish   mailMsgEventPublish;
    /**
     * MessageSettingService
     */
    private final MessageSettingService messageSettingService;
}
