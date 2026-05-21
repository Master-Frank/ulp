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

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.frank.ulp.common.enums.Language;
import cn.frank.ulp.console.pojo.result.setting.SmsTemplateListResult;
import cn.frank.ulp.console.service.setting.SmsTemplateService;
import cn.frank.ulp.support.result.ApiRestResult;

import lombok.AllArgsConstructor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import static cn.frank.ulp.common.constant.SettingConstants.SETTING_PATH;

/**
 * 短信模版配置
 *
 * @author Frank Zhang
 */
@Validated
@Tag(name = "短信模板")
@RestController
@AllArgsConstructor
@RequestMapping(value = SETTING_PATH + "/sms_template", produces = MediaType.APPLICATION_JSON_VALUE)
public class SmsTemplateController {

    /**
     * 获取短信模板列表
     *
     * @param language {@link Language}
     * @return {@link SmsTemplateListResult}
     */
    @GetMapping(value = "/list")
    @Operation(summary = "获取短信模板列表")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(cn.frank.ulp.support.security.userdetails.UserType).ADMIN)")
    public ApiRestResult<List<SmsTemplateListResult>> getSmsTemplateList(@NotNull(message = "语言类型不能为空") @Parameter(description = "语言") Language language) {
        List<SmsTemplateListResult> list = smsTemplateService.getSmsTemplateList(language);
        return ApiRestResult.<List<SmsTemplateListResult>> builder().result(list).build();
    }

    /**
     * 短信模板服务类
     */
    private final SmsTemplateService smsTemplateService;
}
