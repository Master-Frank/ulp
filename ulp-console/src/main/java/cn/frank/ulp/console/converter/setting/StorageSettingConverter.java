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
package cn.frank.ulp.console.converter.setting;

import java.util.Objects;
import java.util.function.Consumer;

import org.jetbrains.annotations.NotNull;
import org.mapstruct.Mapper;
import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import cn.frank.ulp.common.entity.setting.SettingEntity;
import cn.frank.ulp.common.jackjson.encrypt.EncryptionModule;
import cn.frank.ulp.common.storage.StorageConfig;
import cn.frank.ulp.common.storage.StorageProviderException;
import cn.frank.ulp.common.storage.enums.StorageProvider;
import cn.frank.ulp.common.storage.impl.*;
import cn.frank.ulp.console.pojo.result.setting.StorageProviderConfigResult;
import cn.frank.ulp.console.pojo.save.setting.StorageConfigSaveParam;
import cn.frank.ulp.support.validation.ValidationUtils;

import jakarta.validation.ValidationException;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.DefaultTyping;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import static cn.frank.ulp.core.setting.StorageProviderSettingConstants.STORAGE_PROVIDER_KEY;

/**
 * 对象存储设置转换器
 *
 * @author Frank Zhang
 */
@Mapper(componentModel = "spring")
public interface StorageSettingConverter {
    /**
     * 存储提供商配置转实体类
     *
     * @param param {@link StorageConfigSaveParam}
     * @return {@link SettingEntity}
     */
    default SettingEntity storageConfigSaveParamToEntity(StorageConfigSaveParam param) {
        SettingEntity entity = new SettingEntity();
        StorageProvider provider = param.getProvider();
        StorageConfig.StorageConfigBuilder builder = StorageConfig.builder();
        builder.provider(provider);
        ObjectMapper objectMapper = EncryptionModule.deserializerEncrypt();
        try {
            //阿里云
            if (provider.equals(StorageProvider.ALIYUN_OSS)) {
                AliYunOssStorage.Config config = objectMapper
                    .readValue(param.getConfig().toJSONString(), AliYunOssStorage.Config.class);
                config.setDomain(getUrl(config.getDomain()));
                config.setEndpoint(getUrl(config.getEndpoint()));
                builder.config(config);
                validateEntity(ValidationUtils.validateEntity(config));

                AliYunOssStorage.Config unencryptedConfig = new AliYunOssStorage.Config();
                BeanUtils.copyProperties(config, unencryptedConfig);
                unencryptedConfig
                    .setAccessKeySecret(param.getConfig().getString("accessKeySecret"));
                checkStorage(AliYunOssStorage::new, unencryptedConfig);
            }
            //腾讯
            else if (provider.equals(StorageProvider.TENCENT_COS)) {
                TencentCosStorage.Config config = objectMapper
                    .readValue(param.getConfig().toJSONString(), TencentCosStorage.Config.class);
                config.setDomain(getUrl(config.getDomain()));
                builder.config(config);
                validateEntity(ValidationUtils.validateEntity(config));

                TencentCosStorage.Config unencryptedConfig = new TencentCosStorage.Config();
                BeanUtils.copyProperties(config, unencryptedConfig);
                unencryptedConfig.setSecretKey(param.getConfig().getString("secretKey"));
                checkStorage(TencentCosStorage::new, unencryptedConfig);
            }
            //七牛
            else if (provider.equals(StorageProvider.QINIU_KODO)) {
                QiNiuKodoStorage.Config config = objectMapper
                    .readValue(param.getConfig().toJSONString(), QiNiuKodoStorage.Config.class);
                config.setDomain(getUrl(config.getDomain()));
                builder.config(config);
                validateEntity(ValidationUtils.validateEntity(config));

                QiNiuKodoStorage.Config unencryptedConfig = new QiNiuKodoStorage.Config();
                BeanUtils.copyProperties(config, unencryptedConfig);
                unencryptedConfig.setSecretKey(param.getConfig().getString("secretKey"));
                checkStorage(QiNiuKodoStorage::new, unencryptedConfig);
            }
            //Minio
            else if (provider.equals(StorageProvider.MINIO)) {
                MinIoStorage.Config config = objectMapper
                    .readValue(param.getConfig().toJSONString(), MinIoStorage.Config.class);
                config.setEndpoint(getUrl(config.getEndpoint()));
                config.setDomain(getUrl(config.getDomain()));
                builder.config(config);
                validateEntity(ValidationUtils.validateEntity(config));

                MinIoStorage.Config unencryptedConfig = new MinIoStorage.Config();
                BeanUtils.copyProperties(config, unencryptedConfig);
                unencryptedConfig.setSecretKey(param.getConfig().getString("secretKey"));
                checkStorage(MinIoStorage::new, unencryptedConfig);
            }
            //S3
            else if (provider.equals(StorageProvider.S3)) {
                S3Storage.Config config = objectMapper.readValue(param.getConfig().toJSONString(),
                    S3Storage.Config.class);
                config.setEndpoint(getUrl(config.getEndpoint()));
                config.setDomain(getUrl(config.getDomain()));
                builder.config(config);
                validateEntity(ValidationUtils.validateEntity(config));

                S3Storage.Config unencryptedConfig = new S3Storage.Config();
                BeanUtils.copyProperties(config, unencryptedConfig);
                unencryptedConfig
                    .setSecretAccessKey(param.getConfig().getString("secretAccessKey"));
                checkStorage(S3Storage::new, unencryptedConfig);
            }
            entity.setName(STORAGE_PROVIDER_KEY);
            // 指定序列化输入的类型
            ObjectMapper typedMapper = objectMapper.rebuild()
                .activateDefaultTyping(
                    BasicPolymorphicTypeValidator.builder().allowIfSubType(Object.class).build(),
                    DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY)
                .build();
            entity.setValue(typedMapper.writeValueAsString(builder.build()));
            entity.setDesc(provider.getDesc());
            return entity;
        } catch (JacksonException e) {
            throw new RuntimeException(e);
        }
    }

    private static void checkStorage(Consumer<StorageConfig> consumer,
                                     StorageConfig.Config config) {
        try {
            consumer.accept(StorageConfig.builder().config(config).build());
        } catch (Exception e) {
            throw new StorageProviderException("存储配置异常, 请检查配置信息");
        }
    }

    private static void validateEntity(ValidationUtils.ValidationResult<?> validationResult) {
        if (Objects.requireNonNull(validationResult).isHasErrors()) {
            throw new ValidationException(validationResult.getMessage());
        }
    }

    @NotNull
    private static String getUrl(String url) {
        return url.replaceAll("/+$", "");
    }

    /**
     * 实体转存储提供商配置
     *
     * @param entity {@link SettingEntity}
     * @return {@link StorageProviderConfigResult}
     */
    default StorageProviderConfigResult entityToStorageProviderConfig(SettingEntity entity) {
        if (Objects.isNull(entity)) {
            return StorageProviderConfigResult.builder().enabled(false).build();
        }
        ObjectMapper objectMapper = EncryptionModule.deserializerDecrypt().rebuild()
            .activateDefaultTyping(
                BasicPolymorphicTypeValidator.builder().allowIfSubType(Object.class).build(),
                DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY)
            .build();
        try {
            String value = entity.getValue();
            StorageConfig storageConfig = objectMapper.readValue(value, StorageConfig.class);
            // 开启配置、并没有配置
            //@formatter:off
            return StorageProviderConfigResult.builder()
                    .provider(storageConfig.getProvider())
                    .enabled(true)
                    .config(storageConfig.getConfig()).build();
            //@formatter:on
        } catch (JacksonException e) {
            throw new RuntimeException(e);
        }
    }
}
