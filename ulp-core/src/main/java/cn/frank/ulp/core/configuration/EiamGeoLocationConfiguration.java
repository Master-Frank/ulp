/*
 * eiam-core - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.frank.ulp.core.configuration;

import java.io.IOException;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn.frank.ulp.common.entity.setting.SettingEntity;
import cn.frank.ulp.common.geo.GeoLocationProviderConfig;
import cn.frank.ulp.common.geo.NoneGeoLocationParserImpl;
import cn.frank.ulp.common.geo.ip2region.Ip2regionGeoLocationParserImpl;
import cn.frank.ulp.common.geo.maxmind.MaxmindGeoLocationParserImpl;
import cn.frank.ulp.common.geo.maxmind.MaxmindProviderConfig;
import cn.frank.ulp.common.jackjson.encrypt.EncryptionModule;
import cn.frank.ulp.common.repository.setting.SettingRepository;
import cn.frank.ulp.core.setting.GeoIpProviderConstants;
import cn.frank.ulp.support.geo.GeoLocationParser;
import static cn.frank.ulp.common.constant.ConfigBeanNameConstants.GEO_LOCATION;
import static cn.frank.ulp.common.geo.ip2region.Ip2regionGeoLocationParserImpl.IP2REGION;
import static cn.frank.ulp.common.geo.maxmind.MaxmindGeoLocationParserImpl.MAXMIND;

/**
 * 地理位置库
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/11/27 01:58
 */
@Configuration
public class EiamGeoLocationConfiguration {
    private final Logger logger = LoggerFactory.getLogger(EiamGeoLocationConfiguration.class);

    @RefreshScope
    @Bean(value = GEO_LOCATION)
    public GeoLocationParser geoLocation(SettingRepository settingRepository,
                                         RestTemplate restTemplate) {
        try {
            ObjectMapper objectMapper = EncryptionModule.deserializerDecrypt();
            // 指定序列化输入的类型
            objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(),
                ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
            // 查询数据库是否开启地理位置服务
            SettingEntity setting = settingRepository
                .findByName(GeoIpProviderConstants.IPADDRESS_SETTING_NAME);
            if (!Objects.isNull(setting) && StringUtils.isNotBlank(setting.getValue())) {
                GeoLocationProviderConfig provider = objectMapper.readValue(setting.getValue(),
                    GeoLocationProviderConfig.class);
                // maxmind
                if (MAXMIND.equals(provider.getProvider())) {
                    return new MaxmindGeoLocationParserImpl(
                        (MaxmindProviderConfig) provider.getConfig(), restTemplate);
                }
                // ip2region
                if (IP2REGION.equals(provider.getProvider())) {
                    return new Ip2regionGeoLocationParserImpl();
                }
            }
            //没有数据默认使用 ip2region
            return new Ip2regionGeoLocationParserImpl();
        } catch (IOException e) {
            logger.error("Create geo location Exception: {}", e.getMessage(), e);
        }
        return new NoneGeoLocationParserImpl();
    }
}
