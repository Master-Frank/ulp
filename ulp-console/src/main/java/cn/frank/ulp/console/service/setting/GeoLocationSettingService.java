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
