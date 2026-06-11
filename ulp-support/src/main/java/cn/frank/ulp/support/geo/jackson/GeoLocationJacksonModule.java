/*
 * ulp-support - ULP support library
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
package cn.frank.ulp.support.geo.jackson;

import cn.frank.ulp.support.geo.GeoLocation;
import cn.frank.ulp.support.geo.GeoLocationMixin;
import cn.frank.ulp.support.geo.GeoLocationProvider;
import cn.frank.ulp.support.geo.GeoLocationProviderMixin;

import tools.jackson.core.Version;
import tools.jackson.databind.JacksonModule;

public class GeoLocationJacksonModule extends JacksonModule {

    @Override
    public String getModuleName() {
        return GeoLocationJacksonModule.class.getName();
    }

    @Override
    public void setupModule(SetupContext context) {
        context.setMixIn(GeoLocation.class, GeoLocationMixin.class);
        context.setMixIn(GeoLocationProvider.class, GeoLocationProviderMixin.class);
    }

    @Override
    public Version version() {
        return new Version(1, 6, 0, null, null, null);
    }
}
