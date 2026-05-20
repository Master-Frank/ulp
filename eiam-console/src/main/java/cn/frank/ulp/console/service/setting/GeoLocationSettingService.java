/*
 * eiam-console - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.frank.ulp.console.service.setting;

import cn.frank.ulp.console.pojo.result.setting.GeoIpProviderResult;
import cn.frank.ulp.console.pojo.save.setting.GeoIpProviderSaveParam;
import cn.frank.ulp.support.geo.GeoLocation;

/**
 * ip设置接口
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/10/1 21:19
 */
public interface GeoLocationSettingService extends SettingService {

    /**
     * 保存配置
     *
     * @param param {@link GeoIpProviderSaveParam}
     * @return {@link Boolean}
     */
    Boolean saveGeoIpLibrary(GeoIpProviderSaveParam param);

    /**
     * 获取配置
     *
     * @return {@link GeoIpProviderResult}
     */
    GeoIpProviderResult getGeoIpLibrary();

    /**
     * 查询ip详细信息
     *
     * @param ip {@link  String}
     * @return {@link GeoLocation}
     */
    GeoLocation getGeoLocation(String ip);
}
