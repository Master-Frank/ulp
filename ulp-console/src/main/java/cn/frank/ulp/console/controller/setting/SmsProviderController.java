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

import java.util.LinkedHashMap;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import cn.frank.ulp.audit.annotation.Audit;
import cn.frank.ulp.audit.event.type.EventType;
import cn.frank.ulp.common.enums.MailType;
import cn.frank.ulp.common.enums.MessageCategory;
import cn.frank.ulp.common.enums.SmsType;
import cn.frank.ulp.console.pojo.result.setting.SmsProviderConfigResult;
import cn.frank.ulp.console.pojo.save.setting.SmsProviderSaveParam;
import cn.frank.ulp.console.service.setting.MessageSettingService;
import cn.frank.ulp.core.message.MsgVariable;
import cn.frank.ulp.core.message.sms.SmsMsgEventPublish;
import cn.frank.ulp.support.demo.Preview;
import cn.frank.ulp.support.lock.Lock;
import cn.frank.ulp.support.result.ApiRestResult;
import cn.frank.ulp.support.security.password.PasswordGenerator;

import lombok.AllArgsConstructor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import static cn.frank.ulp.common.constant.SettingConstants.SETTING_PATH;
import static cn.frank.ulp.core.message.MsgVariable.EXPIRE_DAYS;
import static cn.frank.ulp.core.message.sms.SmsMsgEventPublish.USERNAME;

/**
 * 消息设置
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/7/24 22:36
 */
@Validated
@Tag(name = "短信提供商")
@RestController
@AllArgsConstructor
@RequestMapping(value = SETTING_PATH + "/message/sms_provider")
public class SmsProviderController {
    /**
     * 禁用短信提供商
     *
     * @return {@link ApiRestResult}
     */
    @Lock
    @Preview
    @Validated
    @Operation(summary = "禁用短信提供商")
    @Audit(type = EventType.OFF_SMS_SERVICE)
    @PutMapping(value = "/disable")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(cn.frank.ulp.support.security.userdetails.UserType).ADMIN)")
    public ApiRestResult<Boolean> disableSmsProvider() {
        Boolean result = messageSettingService.disableSmsProvider();
        return ApiRestResult.ok(result);
    }

    /**
     * 保存短信提供商配置
     *
     * @param param {@link SmsProviderSaveParam}
     * @return {@link ApiRestResult}
     */
    @Lock
    @Preview
    @Validated
    @Operation(summary = "保存短信提供商配置")
    @Audit(type = EventType.SAVE_SMS_SERVICE)
    @PostMapping("save")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(cn.frank.ulp.support.security.userdetails.UserType).ADMIN)")
    public ApiRestResult<Boolean> saveSmsProviderConfig(@RequestBody SmsProviderSaveParam param) {
        Boolean result = messageSettingService.saveSmsProviderConfig(param);
        return ApiRestResult.ok(result);
    }

    /**
     * 获取短信提供商配置
     *
     * @return {@link ApiRestResult}
     */
    @Operation(summary = "获取短信提供商配置")
    @GetMapping("/config")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(cn.frank.ulp.support.security.userdetails.UserType).ADMIN)")
    public ApiRestResult<SmsProviderConfigResult> getSmsProviderConfig() {
        SmsProviderConfigResult result = messageSettingService.getSmsProviderConfig();
        return ApiRestResult.ok(result);
    }

    /**
     * 短信发送测试
     *
     * @param smsType {@link MailType}
     * @param receiver {@link String}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "发送测试短信")
    @GetMapping(value = "/test")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(cn.frank.ulp.support.security.userdetails.UserType).ADMIN)")
    public ApiRestResult<Boolean> sendSms(SmsType smsType, String receiver) {
        if (MessageCategory.NOTICE.equals(smsType.getCategory())) {
            LinkedHashMap<String, String> parameter = new LinkedHashMap<>(16);
            boolean flag = SmsType.WELCOME_SMS == smsType || SmsType.RESET_PASSWORD == smsType
                           || SmsType.RESET_PASSWORD_SUCCESS == smsType;
            if (flag) {
                parameter.put(USERNAME, "test");
            }
            if (flag) {
                parameter.put(MsgVariable.PASSWORD, passwordGenerator.generatePassword());
            }
            if (SmsType.PASSWORD_SOON_EXPIRED_REMIND == smsType) {
                parameter.put(EXPIRE_DAYS, "3");
            }
            smsMsgEventPublish.publish(smsType, receiver, parameter);
        } else {
            smsMsgEventPublish.publishVerifyCode(receiver, smsType, "123456");
        }
        return ApiRestResult.ok();
    }

    /**
     * MessageSettingService
     */
    private final MessageSettingService messageSettingService;

    private final SmsMsgEventPublish    smsMsgEventPublish;

    private final PasswordGenerator     passwordGenerator;
}
