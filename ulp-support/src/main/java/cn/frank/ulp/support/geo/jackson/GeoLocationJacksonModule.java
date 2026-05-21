/*
 * ULP - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.frank.ulp.support.geo.jackson;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.Module;

import cn.frank.ulp.support.geo.GeoLocation;
import cn.frank.ulp.support.geo.GeoLocationMixin;
import cn.frank.ulp.support.geo.GeoLocationProvider;
import cn.frank.ulp.support.geo.GeoLocationProviderMixin;

public class GeoLocationJacksonModule extends Module {

    @Override
    public String getModuleName() {
        return GeoLocationJacksonModule.class.getName();
    }

    @Override
    public void setupModule(SetupContext context) {
        context.setMixInAnnotations(GeoLocation.class, GeoLocationMixin.class);
        context.setMixInAnnotations(GeoLocationProvider.class, GeoLocationProviderMixin.class);
    }

    @Override
    public Version version() {
        return new Version(1, 6, 0, null, null, null);
    }
}
