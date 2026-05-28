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

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.frank.ulp.common.entity.setting.SettingEntity;
import cn.frank.ulp.common.repository.setting.SettingRepository;
import cn.frank.ulp.console.converter.setting.GeoLocationSettingConverter;
import cn.frank.ulp.console.pojo.result.setting.GeoIpProviderResult;
import cn.frank.ulp.console.pojo.save.setting.GeoIpProviderSaveParam;
import cn.frank.ulp.console.service.setting.GeoLocationSettingService;
import cn.frank.ulp.support.context.ApplicationContextService;
import cn.frank.ulp.support.geo.GeoLocation;
import cn.frank.ulp.support.geo.GeoLocationParser;

import lombok.extern.slf4j.Slf4j;
import static cn.frank.ulp.common.constant.ConfigBeanNameConstants.GEO_LOCATION;
import static cn.frank.ulp.core.setting.GeoIpProviderConstants.IPADDRESS_SETTING_NAME;

/**
 * ip设置接口
 *
 * @author Frank Zhang
 */
@Slf4j
@Service
public class GeoLocationSettingServiceImpl extends SettingServiceImpl
                                           implements GeoLocationSettingService {

    /**
     * 保存配置
     *
     * @param param {@link GeoIpProviderSaveParam}
     * @return {@link Boolean}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveGeoIpLibrary(GeoIpProviderSaveParam param) {
        SettingEntity settingEntity = geoLocationSettingsConverter
            .geoLocationProviderConfigToEntity(param);
        Boolean success = saveSetting(settingEntity);
        ApplicationContextService.refresh(GEO_LOCATION);
        return success;
    }

    /**
     * 获取配置
     *
     * @return {@link SettingEntity}
     */
    @Override
    public GeoIpProviderResult getGeoIpLibrary() {
        return geoLocationSettingsConverter
            .entityToGeoLocationProviderConfig(getSetting(IPADDRESS_SETTING_NAME));
    }

    /**
     * 查询ip详细信息
     *
     * @param ip {@link String}
     * @return {@link GeoLocation}
     */
    @Override
    public GeoLocation getGeoLocation(String ip) {
        return geoLocationParser.getGeoLocation(ip);
    }

    private final GeoLocationParser           geoLocationParser;
    private final GeoLocationSettingConverter geoLocationSettingsConverter;

    public GeoLocationSettingServiceImpl(SettingRepository settingsRepository,
                                         GeoLocationParser geoLocationParser,
                                         GeoLocationSettingConverter geoLocationSettingsConverter) {
        super(settingsRepository);
        this.geoLocationParser = geoLocationParser;
        this.geoLocationSettingsConverter = geoLocationSettingsConverter;
    }
}
