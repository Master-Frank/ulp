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
package cn.frank.ulp.console.service.setting.impl;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import cn.frank.ulp.common.enums.Language;
import cn.frank.ulp.common.enums.SmsType;
import cn.frank.ulp.common.repository.setting.SettingRepository;
import cn.frank.ulp.console.pojo.result.setting.SmsTemplateListResult;
import cn.frank.ulp.console.service.setting.SmsTemplateService;

/**
 * <p>
 * 短信模版模板 服务实现类
 * </p>
 *
 * @author Frank Zhang
 */
@Service
public class SmsTemplateServiceImpl extends SettingServiceImpl implements SmsTemplateService {

    /**
     * 获取短信模版列表
     *
     * @param language {@link Language}
     * @return {@link SmsTemplateListResult}
     */
    @Override
    public List<SmsTemplateListResult> getSmsTemplateList(Language language) {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("sms/template/sms-template",
            new Locale(language.getLocale()));
        List<SmsTemplateListResult> results = Lists.newArrayList();
        SmsType[] values = SmsType.values();

        for (SmsType type : values) {
            SmsTemplateListResult result = new SmsTemplateListResult();
            String content = resourceBundle.getString(type.getCode());
            result.setContent(content);
            result.setType(type);
            result.setName(type.getDesc());
            result.setLanguage(Language.getType(resourceBundle.getLocale().getLanguage()));
            results.add(result);
        }
        return results;
    }

    public SmsTemplateServiceImpl(SettingRepository settingsRepository) {
        super(settingsRepository);
    }

}
