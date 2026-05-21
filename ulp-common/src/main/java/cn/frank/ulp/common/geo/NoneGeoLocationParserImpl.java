/*
 * eiam-common - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.frank.ulp.common.geo;

import cn.frank.ulp.support.geo.GeoLocation;
import cn.frank.ulp.support.geo.GeoLocationParser;
import cn.frank.ulp.support.geo.GeoLocationProvider;

/**
 * None
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/11/27 21:20
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
