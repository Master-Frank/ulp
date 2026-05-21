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

import cn.frank.ulp.support.geo.GeoLocation;
import cn.frank.ulp.support.geo.GeoLocationParser;
import cn.frank.ulp.support.geo.GeoLocationProvider;

/**
 * None
 *
 * @author Frank Zhang
 */
public class NoneGeoLocationParserImpl implements GeoLocationParser {

    /**
     * 获取地理位置
     *
     * @param remote {@link String}
     * @return {@link GeoLocation}
     */
    @Override
    public GeoLocation getGeoLocation(String remote) {
        return GeoLocation.builder().ip(remote).provider(GeoLocationProvider.NONE).build();
    }
}
