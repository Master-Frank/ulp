/*
 * ulp-audit - United Login Platform
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
package cn.frank.ulp.audit.entity;

import java.io.Serial;
import java.io.Serializable;

import cn.frank.ulp.support.geo.GeoLocationProvider;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 地理位置
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/11/5 23:31
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GeoLocation implements Serializable {

    @Serial
    private static final long   serialVersionUID           = -1144169992714000310L;

    public static final String  GEO_LOCATION_PROVINCE_CODE = "geo_location.province_code.keyword";

    /**
     * IP
     */
    private String              ip;

    /**
     * continent code
     */
    private String              continentCode;

    /**
     * continent Name
     */
    private String              continentName;

    /**
     * 国家code
     */
    private String              countryCode;

    /**
     * 国家名称
     */
    private String              countryName;

    /**
     * 省份code
     */
    private String              provinceCode;

    /**
     * 省份
     */
    private String              provinceName;

    /**
     * 城市code
     */
    private String              cityCode;

    /**
     * 城市名称
     */
    private String              cityName;

    /**
     * 地理坐标
     */
    private GeoPoint            point;

    /**
     * 提供商
     */
    private GeoLocationProvider provider;

}
