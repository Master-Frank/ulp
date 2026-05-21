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
package cn.frank.ulp.console.service.setting;

import java.util.List;

import cn.frank.ulp.common.enums.Language;
import cn.frank.ulp.console.pojo.result.setting.SmsTemplateListResult;

/**
 * <p>
 * 短信模版 服务类
 * </p>
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020-08-13
 */
public interface SmsTemplateService extends SettingService {

    /**
     * 获取短信模版列表
     *
     * @param language {@link Language}
     * @return {@link SmsTemplateListResult}
     */
    List<SmsTemplateListResult> getSmsTemplateList(Language language);
}
