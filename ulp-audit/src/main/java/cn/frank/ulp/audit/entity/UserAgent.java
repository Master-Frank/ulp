/*
 * ulp-audit - United Login Platform
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
package cn.frank.ulp.audit.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * UserAgent
 * @author TopIAM
 * Created by support@topiam.cn on 2022/11/5 23:31
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserAgent implements Serializable {

    public static final String USER_AGENT_BROWSER = "user_agent.browser.keyword";

    private String             deviceType;

    private String             platform;

    private String             platformVersion;

    private String             browser;

    private String             browserType;

    private String             browserMajorVersion;
}
