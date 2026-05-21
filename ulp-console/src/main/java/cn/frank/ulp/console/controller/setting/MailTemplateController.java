/*
 * eiam-console - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.frank.ulp.console.controller.setting;

import java.util.List;
import java.util.Objects;

import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import cn.frank.ulp.audit.annotation.Audit;
import cn.frank.ulp.audit.event.type.EventType;
import cn.frank.ulp.common.enums.MailType;
import cn.frank.ulp.console.pojo.result.setting.EmailTemplateListResult;
import cn.frank.ulp.console.pojo.result.setting.EmailTemplateResult;
import cn.frank.ulp.console.pojo.save.setting.EmailCustomTemplateSaveParam;
import cn.frank.ulp.console.service.setting.MailTemplateService;
import cn.frank.ulp.support.demo.Preview;
import cn.frank.ulp.support.lock.Lock;
import cn.frank.ulp.support.result.ApiRestResult;

import lombok.AllArgsConstructor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import static cn.frank.ulp.common.constant.SettingConstants.SETTING_PATH;

/**
 * 邮件配置
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/7/24 22:36
 */
@Validated
@Tag(name = "邮件模板")
@RestController
@AllArgsConstructor
@RequestMapping(value = SETTING_PATH
                        + "/mail_template", produces = MediaType.APPLICATION_JSON_VALUE)
public class MailTemplateController {

    /**
     * 获取邮件模板列表
     *
     * @return {@link EmailTemplateResult}
     */
    @Validated
    @GetMapping(value = "/list")
    @Operation(summary = "获取邮件模板列表")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(cn.frank.ulp.support.security.userdetails.UserType).ADMIN)")
    public ApiRestResult<List<EmailTemplateListResult>> getEmailTemplateList() {
        List<EmailTemplateListResult> list = templateService.getEmailTemplateList();
        return ApiRestResult.<List<EmailTemplateListResult>> builder().result(list).build();
    }

    /**
     * 根据模板类型查询邮件模板
     *
     * @param type {@link String}
     * @return {@link EmailTemplateResult}
     */
    @Operation(summary = "获取邮件模板信息")
    @GetMapping(value = "/{type}")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(cn.frank.ulp.support.security.userdetails.UserType).ADMIN)")
    public ApiRestResult<EmailTemplateResult> getEmailTemplate(@PathVariable(value = "type") String type) {
        MailType templateType = MailType.getType(type);
        EmailTemplateResult result = templateService.getEmailTemplate(templateType);
        return ApiRestResult.<EmailTemplateResult> builder().result(result).build();
    }

    /**
     * 保存邮件模板
     *
     * @param type {@link String}
     * @return {@link EmailTemplateResult}
     */
    @Lock
    @Preview
    @PutMapping(value = "/save_custom/{type}")
    @Audit(type = EventType.SAVE_MAIL_TEMPLATE)
    @Operation(summary = "保存邮件模板")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(cn.frank.ulp.support.security.userdetails.UserType).ADMIN)")
    public ApiRestResult<Boolean> saveCustomEmailTemplate(@PathVariable(value = "type") String type,
                                                          @RequestBody @Validated EmailCustomTemplateSaveParam param) {
        MailType templateType = MailType.getType(type);
        return ApiRestResult.<Boolean> builder()
            .result(!Objects.isNull(templateService.saveCustomEmailTemplate(templateType, param)))
            .build();
    }

    /**
     * 关闭自定义邮件模板
     *
     * @return {@link EmailTemplateResult}
     */
    @Lock
    @Preview
    @PutMapping(value = "/disable_custom/{type}")
    @Operation(summary = "禁用自定义邮件模板")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(cn.frank.ulp.support.security.userdetails.UserType).ADMIN)")
    public ApiRestResult<Boolean> disableCustomEmailTemplate(@PathVariable(value = "type") String type) {
        templateService.disableCustomEmailTemplate(MailType.getType(type));
        return ApiRestResult.ok();
    }

    /**
     * 邮件模板服务类
     */
    private final MailTemplateService templateService;
}
