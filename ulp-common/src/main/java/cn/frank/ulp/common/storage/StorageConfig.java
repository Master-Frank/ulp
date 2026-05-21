/*
 * ulp-common - United Login Platform
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
package cn.frank.ulp.common.storage;

import org.hibernate.validator.constraints.URL;

import cn.frank.ulp.common.storage.enums.StorageProvider;

import lombok.Builder;
import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import static cn.frank.ulp.common.storage.impl.AliYunOssStorage.URL_REGEXP;

/**
 * 存储配置
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/10/1 21:10
 */
@Data
@Builder
public class StorageConfig {

    public StorageConfig() {
    }

    public StorageConfig(StorageProvider provider) {
        this.provider = provider;
    }

    public StorageConfig(StorageProvider provider, Config config) {
        this.provider = provider;
        this.config = config;
    }

    /**
     * 平台
     */
    @NotEmpty(message = "平台类型不能为空")
    private StorageProvider provider;

    /**
     * 配置
     */
    private Config          config;

    /**
     * Config
     */
    @Data
    public static class Config {

        /**
         * 域名
         */
        @URL(message = "访问域名格式不正确", regexp = URL_REGEXP)
        @NotEmpty(message = "访问域名不能为空")
        private String domain;
        /**
         * 存储位置
         */
        private String location = "TopIAM";
    }
}
