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
package cn.frank.ulp.support.web.useragent;

import com.blueconic.browscap.BrowsCapField;
import com.blueconic.browscap.Capabilities;
import com.blueconic.browscap.UserAgentService;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 默认用户代理解析器实现类
 * 使用browscap库解析User-Agent信息
 */
public class DefaultUserAgentParser implements UserAgentParser {
    /**
    * browscap用户代理解析器
    */
    private final com.blueconic.browscap.UserAgentParser userAgentParser;

    /**
    * 获取用户代理信息
    *
    * @param request HTTP请求
    * @return 用户代理信息
    */
    @Override
    public UserAgent getUserAgent(HttpServletRequest request) {
        Capabilities capabilities = this.userAgentParser.parse(request.getHeader("User-Agent"));
        String browser = capabilities.getBrowser();
        String browserType = capabilities.getBrowserType();
        String browserMajorVersion = capabilities.getBrowserMajorVersion();
        String deviceType = capabilities.getDeviceType();
        String platform = capabilities.getPlatform();
        String platformVersion = capabilities.getPlatformVersion();
        String renderingEngineMaker = capabilities.getValue(BrowsCapField.RENDERING_ENGINE_MAKER);
        return UserAgent.builder().browser(browser).browserType(browserType)
            .browserMajorVersion(browserMajorVersion).deviceType(deviceType).platform(platform)
            .platformVersion(platformVersion).renderingEngineMaker(renderingEngineMaker).build();
    }

    /**
    * 默认构造函数
    */
    public DefaultUserAgentParser() {
        super();

        try {
            this.userAgentParser = (new UserAgentService()).loadParser();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
