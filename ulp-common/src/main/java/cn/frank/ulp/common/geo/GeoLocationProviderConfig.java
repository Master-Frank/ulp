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
package cn.frank.ulp.common.geo;

import java.io.Serial;
import java.io.Serializable;

import cn.frank.ulp.support.geo.GeoLocationProvider;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

/**
 * MaxmindProviderSetting
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/10/1 21:10
 */
@Data
public class GeoLocationProviderConfig {

    public GeoLocationProviderConfig() {
    }

    public GeoLocationProviderConfig(GeoLocationProvider provider, GeoLocationConfig config) {
        this.provider = provider;
        this.config = config;
    }

    /**
     * 平台
     */
    @NotNull(message = "平台不能为空")
    private GeoLocationProvider provider;

    /**
     * 配置
     */
    private GeoLocationConfig   config;

    @Data
    public static class GeoLocationConfig implements Serializable {
        @Serial
        private static final long serialVersionUID = 5611656522133230183L;
    }

}
