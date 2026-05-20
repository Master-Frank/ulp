/*
 * eiam-console - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.frank.ulp.console.initializer;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn.frank.ulp.common.entity.setting.SettingEntity;
import cn.frank.ulp.common.geo.GeoLocationProviderConfig;
import cn.frank.ulp.common.jackjson.encrypt.EncryptionModule;
import cn.frank.ulp.common.repository.setting.SettingRepository;
import cn.frank.ulp.support.config.AbstractSystemInitializer;
import cn.frank.ulp.support.config.InitializationException;
import static cn.frank.ulp.common.geo.ip2region.Ip2regionGeoLocationParserImpl.IP2REGION;
import static cn.frank.ulp.core.setting.GeoIpProviderConstants.IPADDRESS_SETTING_NAME;

/**
 * GeoIpProviderInitializer
 *
 * @author TOPIAM
 * Created by support@topiam.cn on 2024/11/3 18:11
 */
@Component
public class GeoIpProviderInitializer extends AbstractSystemInitializer {

    private final Logger logger = LoggerFactory.getLogger(GeoIpProviderInitializer.class);

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void init() throws InitializationException {
        //@formatter:off
        try {
            SettingEntity optional = settingRepository.findByName(IPADDRESS_SETTING_NAME);
            if (Objects.isNull(optional)) {
                logger.info("初始化系统默认IP地址提供商");
                SettingEntity setting = new SettingEntity();
                setting.setName(IPADDRESS_SETTING_NAME);
                ObjectMapper objectMapper = EncryptionModule.deserializerDecrypt();
                // 指定序列化输入的类型
                objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
                setting.setValue(objectMapper.writeValueAsString(new GeoLocationProviderConfig(IP2REGION, null)));
                setting.setDesc(IP2REGION.getName());
                setting.setRemark("The system initializes the default configuration.");
                settingRepository.save(setting);
            }
        } catch (JsonProcessingException e) {
            throw new InitializationException(e);
        }
        //@formatter:on
    }

    @Override
    public int getOrder() {
        return 2;
    }

    /**
     * SettingRepository
     */
    private final SettingRepository settingRepository;

    /**
     *
     * @param settingRepository {@link SettingRepository}
     */
    public GeoIpProviderInitializer(SettingRepository settingRepository) {
        this.settingRepository = settingRepository;
    }
}
