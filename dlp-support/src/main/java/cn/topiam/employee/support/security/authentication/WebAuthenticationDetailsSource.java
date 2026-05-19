/*
 * ULP - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.support.security.authentication;

import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.util.Assert;

import cn.topiam.employee.support.geo.GeoLocationParser;
import cn.topiam.employee.support.util.IpUtils;
import cn.topiam.employee.support.web.useragent.UserAgentParser;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Web认证详情源类
 * 用于创建Web认证详情对象
 */
public class WebAuthenticationDetailsSource implements AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> {
   /**
    * 用户代理解析器
    */
   private final UserAgentParser userAgentParser;
   
   /**
    * 地理位置解析器
    */
   private final GeoLocationParser geoLocationParser;

   /**
    * 构建认证详情
    *
    * @param request HTTP请求
    * @return Web认证详情
    */
   @Override
   public WebAuthenticationDetails buildDetails(HttpServletRequest request) {
      GeoLocationParser geoLocation = this.geoLocationParser.getGeoLocation(IpUtils.getIpAddr(request));
      return new WebAuthenticationDetails(request, this.userAgentParser.getUserAgent(request), geoLocation);
   }

   /**
    * 构造函数
    *
    * @param geoLocationParser 地理位置解析器
    * @param userAgentParser 用户代理解析器
    */
   public WebAuthenticationDetailsSource(GeoLocationParser geoLocationParser, UserAgentParser userAgentParser) {
      Assert.notNull(userAgentParser, "用户代理解析器不能为空");
      Assert.notNull(geoLocationParser, "地理位置解析器不能为空");
      this.userAgentParser = userAgentParser;
      this.geoLocationParser = geoLocationParser;
   }
}
