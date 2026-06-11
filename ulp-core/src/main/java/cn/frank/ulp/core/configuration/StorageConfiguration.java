/*
 * ulp-core - United Login Platform
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
package cn.frank.ulp.core.configuration;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import tools.jackson.databind.ObjectMapper;

import cn.frank.ulp.common.entity.setting.SettingEntity;
import cn.frank.ulp.common.jackjson.encrypt.EncryptionModule;
import cn.frank.ulp.common.repository.setting.SettingRepository;
import cn.frank.ulp.common.storage.Storage;
import cn.frank.ulp.common.storage.StorageConfig;
import cn.frank.ulp.common.storage.StorageFactory;
import cn.frank.ulp.common.storage.impl.NoneStorage;
import cn.frank.ulp.core.setting.StorageProviderSettingConstants;

import lombok.extern.slf4j.Slf4j;

/**
 * 存储配置
 *
 * @author Frank Zhang
 */
@Slf4j
@Configuration
public class StorageConfiguration {

    /**
     * 存储配置
     *
     * @return {@link Storage}
     */
    @Bean(name = StorageProviderSettingConstants.STORAGE_BEAN_NAME)
    @RefreshScope
    public Storage storage() {
        SettingEntity setting = repository
            .findByName(StorageProviderSettingConstants.STORAGE_PROVIDER_KEY);
        ObjectMapper objectMapper = EncryptionModule.deserializerDecrypt();
        // 指定序列化输入的类型
        objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(),
            ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        try {
            if (!Objects.isNull(setting) && StringUtils.isNotEmpty(setting.getValue())) {
                return StorageFactory
                    .getStorage(objectMapper.readValue(setting.getValue(), StorageConfig.class));
            }
        } catch (Exception e) {
            log.error("Create storage Exception: {}", e.getMessage(), e);
        }
        return new NoneStorage();
    }

    private final SettingRepository repository;

    public StorageConfiguration(SettingRepository settingRepository) {
        this.repository = settingRepository;
    }

}
