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
package cn.frank.ulp.support.web.useragent;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 用户代理解析器接口
 * 用于解析HTTP请求中的User-Agent信息
 */
public interface UserAgentParser {

    /**
    * 获取用户代理信息
    *
    * @param request HTTP请求
    * @return 用户代理信息
    */
    UserAgent getUserAgent(HttpServletRequest request);
}