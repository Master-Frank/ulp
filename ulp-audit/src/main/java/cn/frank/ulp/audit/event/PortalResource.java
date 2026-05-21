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
package cn.frank.ulp.audit.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 审计资源
 *
 * @author Frank Zhang
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class PortalResource extends Resource {
    /**
     * 资源编码
     */
    private String code;
    /**
     * 资源名称
     */
    private String name;

    @Override
    public String toString() {
        return String.format("[%s](%s)", name, code);
    }

    /**
     * 组织与用户
     */
    public static PortalResource MY_ACCOUNT_RESOURCE = new PortalResource(
        "eiam:event:resource:my_account", "我的账户");

    /**
     * 用户组管理
     */
    public static PortalResource MY_APP_RESOURCE     = new PortalResource(
        "eiam:event:resource:my_app", "我的应用");

    /**
     * 会话管理
     */
    public static PortalResource SESSION_RESOURCE    = new PortalResource(
        "eiam:event:resource:session", "会话管理");
}