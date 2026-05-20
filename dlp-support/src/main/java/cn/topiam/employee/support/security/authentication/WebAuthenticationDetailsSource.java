/*
 * ULP - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.support.security.authentication;

import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.util.Assert;

import cn.topiam.employee.support.geo.GeoLocation;
import cn.topiam.employee.support.geo.GeoLocationParser;
import cn.topiam.employee.support.util.IpUtils;
import cn.topiam.employee.support.web.useragent.UserAgent;
import cn.topiam.employee.support.web.useragent.UserAgentParser;

import jakarta.servlet.http.HttpServletRequest;

public class WebAuthenticationDetailsSource
    implements AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> {

    private final GeoLocationParser geoLocationParser;

    private final UserAgentParser userAgentParser;

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
