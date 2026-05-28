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
package cn.frank.ulp.support.web.jackjson;

import java.util.ArrayList;
import java.util.List;

import cn.frank.ulp.support.web.useragent.UserAgent;

/**
 * 用户代理构建器Mixin类
 * 用于扩展用户代理构建器功能
 */
public class UserAgentBuilderMixin {

    /**
    * 用户代理列表
    */
    private List<UserAgent> userAgents = new ArrayList<>();

    /**
    * 添加用户代理
    *
    * @param userAgent 用户代理
    * @return 用户代理构建器Mixin
    */
    public UserAgentBuilderMixin addUserAgent(UserAgent userAgent) {
        this.userAgents.add(userAgent);
        return this;
    }

    /**
    * 获取用户代理列表
    *
    * @return 用户代理列表
    */
    public List<UserAgent> getUserAgents() {
        return this.userAgents;
    }
}
