/*
 * eiam-console - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.frank.ulp.console.service.setting;

import cn.frank.ulp.console.pojo.result.setting.SecurityBasicConfigResult;
import cn.frank.ulp.console.pojo.save.setting.SecurityBasicSaveParam;

/**
 * <p>
 * 安全设置表 服务类
 * </p>
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020-10-01
 */
public interface SecuritySettingService extends SettingService {

    /**
     * 获取配置
     *
     * @return {@link SecurityBasicConfigResult}
     */
    SecurityBasicConfigResult getBasicConfig();

    /**
     * 保存配置
     *
     * @param param {@link SecurityBasicSaveParam}
     * @return {@link Boolean}
     */
    Boolean saveBasicConfig(SecurityBasicSaveParam param);

}
