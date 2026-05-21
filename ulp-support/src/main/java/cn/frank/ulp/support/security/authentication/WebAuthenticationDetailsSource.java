/*
 * ulp-support - United Login Platform
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
package cn.frank.ulp.support.security.authentication;

import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.util.Assert;

import cn.frank.ulp.support.geo.GeoLocation;
import cn.frank.ulp.support.geo.GeoLocationParser;
import cn.frank.ulp.support.util.IpUtils;
import cn.frank.ulp.support.web.useragent.UserAgent;
import cn.frank.ulp.support.web.useragent.UserAgentParser;

import jakarta.servlet.http.HttpServletRequest;

public class WebAuthenticationDetailsSource implements
                                            AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> {

    private final GeoLocationParser geoLocationParser;

    private final UserAgentParser   userAgentParser;

    public WebAuthenticationDetailsSource(GeoLocationParser geoLocationParser,
                                          UserAgentParser userAgentParser) {
        Assert.notNull(geoLocationParser, "GeoLocationParser must not be null");
        Assert.notNull(userAgentParser, "UserAgentParser must not be null");
        this.geoLocationParser = geoLocationParser;
        this.userAgentParser = userAgentParser;
    }

    @Override
    public WebAuthenticationDetails buildDetails(HttpServletRequest request) {
        GeoLocation geoLocation = this.geoLocationParser.getGeoLocation(IpUtils.getIpAddr(request));
        UserAgent userAgent = this.userAgentParser.getUserAgent(request);
        return new WebAuthenticationDetails(request, userAgent, geoLocation);
    }
}
