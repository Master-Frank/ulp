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
package cn.frank.ulp.audit.event.type;

import java.util.List;

import cn.frank.ulp.audit.event.ConsoleResource;
import cn.frank.ulp.audit.event.Type;
import cn.frank.ulp.support.security.userdetails.UserType;
import static cn.frank.ulp.audit.event.ConsoleResource.IDP_RESOURCE;

/**
 * 认证资源
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/11/24 22:59
 */
public class AuthenticationEventType {

    /**
     * 添加身份提供商
     */
    public static Type ADD_IDP        = new Type("eiam:event:idp_add", "添加身份提供商", IDP_RESOURCE,
        List.of(UserType.ADMIN));
    /**
     * 编辑身份提供商
     */
    public static Type UPDATE_IDP     = new Type("eiam:event:idp_update", "修改身份提供商", IDP_RESOURCE,
        List.of(UserType.ADMIN));
    /**
     * 启用身份提供商
     */
    public static Type ENABLE_IDP     = new Type("eiam:event:idp_enabled", "启用身份提供商", IDP_RESOURCE,
        List.of(UserType.ADMIN));
    /**
     * 禁用身份提供商
     */
    public static Type DISABLE_IDP    = new Type("eiam:event:idp_disabled", "禁用身份提供商", IDP_RESOURCE,
        List.of(UserType.ADMIN));
    /**
     * 删除身份提供商
     */
    public static Type DELETE_IDP     = new Type("eiam:event:idp_delete", "删除身份提供商", IDP_RESOURCE,
        List.of(UserType.ADMIN));

    /**
     * 登录控制台
     */
    public static Type LOGIN_CONSOLE  = new Type("eiam:event:login:console", "登录控制台",
        ConsoleResource.AUTHENTICATION_RESOURCE, List.of(UserType.ADMIN));

    /**
     * 退出控制台
     */
    public static Type LOGOUT_CONSOLE = new Type("eiam:event:logout:console", "退出控制台",
        ConsoleResource.AUTHENTICATION_RESOURCE, List.of(UserType.ADMIN));

}
