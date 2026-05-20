/*
 * eiam-console - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
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
