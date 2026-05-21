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

import cn.frank.ulp.audit.event.Type;
import cn.frank.ulp.support.security.userdetails.UserType;
import static cn.frank.ulp.audit.event.ConsoleResource.APP_GROUP_RESOURCE;
import static cn.frank.ulp.audit.event.ConsoleResource.APP_RESOURCE;

/**
 * 应用资源
 *
 * @author Frank Zhang
 */
public class AppEventType {

    /**
     * 添加应用
     */
    public static Type ADD_APP                         = new Type("eiam:event:app:create", "添加应用",
        APP_RESOURCE, List.of(UserType.ADMIN));
    /**
     * 启用应用
     */
    public static Type ENABLE_APP                      = new Type("eiam:event:app:enabled", "启用应用",
        APP_RESOURCE, List.of(UserType.ADMIN));
    /**
     * 禁用应用
     */
    public static Type DISABLE_APP                     = new Type("eiam:event:app:disabled", "禁用应用",
        APP_RESOURCE, List.of(UserType.ADMIN));
    /**
     * 编辑应用
     */
    public static Type UPDATE_APP                      = new Type("eiam:event:app:update", "修改应用",
        APP_RESOURCE, List.of(UserType.ADMIN));
    /**
     * 保存应用配置
     */
    public static Type SAVE_APP_CONFIG                 = new Type("eiam:event:app:save:config",
        "保存应用配置", APP_RESOURCE, List.of(UserType.ADMIN));

    /**
     * 删除应用
     */
    public static Type DELETE_APP                      = new Type("eiam:event:app:delete", "删除应用",
        APP_RESOURCE, List.of(UserType.ADMIN));
    /**
     * 应用访问授权
     */
    public static Type APP_ACCESS_POLICY               = new Type("eiam:event:app:access_policy",
        "添加应用访问授权", APP_RESOURCE, List.of(UserType.ADMIN));

    /**
     * 启用应用访问授权
     */
    public static Type ENABLE_APP_ACCESS_POLICY        = new Type(
        "eiam:event:app:enable_access_policy", "启用应用访问授权", APP_RESOURCE, List.of(UserType.ADMIN));

    /**
     * 禁用应用访问授权
     */
    public static Type DISABLE_APP_ACCESS_POLICY       = new Type(
        "eiam:event:app:disable_access_policy", "禁用应用访问授权", APP_RESOURCE, List.of(UserType.ADMIN));

    /**
     * 删除访问授权
     */
    public static Type APP_CANCEL_ACCESS_POLICY        = new Type(
        "eiam:event:app:cancel_access_policy", "取消应用访问授权", APP_RESOURCE, List.of(UserType.ADMIN));

    /**
     * 添加应用账户
     */
    public static Type ADD_APP_ACCOUNT                 = new Type("eiam:event:app:add_app_account",
        "添加应用账户", APP_RESOURCE, List.of(UserType.ADMIN));

    /**
     * 更新默认应用账户
     */
    public static Type UPDATE_DEFAULT_APP_ACCOUNT      = new Type(
        "eiam:event:app:update_default_app_account", "更新默认应用账户", APP_RESOURCE,
        List.of(UserType.ADMIN));
    /**
     * 删除应用账户
     */
    public static Type DELETE_APP_ACCOUNT              = new Type(
        "eiam:event:app:delete_app_account", "删除应用账户", APP_RESOURCE, List.of(UserType.ADMIN));

    /**
     * 保存SSO配置
     */
    public static Type SAVE_SSO_CONFIG                 = new Type("eiam:event:app:save_sso_config",
        "保存SSO配置", APP_RESOURCE, List.of(UserType.ADMIN));

    /**
     * 保存应用权限资源
     */
    public static Type SAVE_APP_PERMISSION_RESOURCE    = new Type(
        "eiam:event:app:save_app_permission_resource", "保存应用权限资源", APP_RESOURCE,
        List.of(UserType.ADMIN));

    /**
     * 修改应用权限资源
     */
    public static Type UPDATE_APP_PERMISSION_RESOURCE  = new Type(
        "eiam:event:app:update_app_permission_resource", "修改应用权限资源", APP_RESOURCE,
        List.of(UserType.ADMIN));

    /**
     * 删除应用权限资源
     */
    public static Type DELETE_APP_PERMISSION_RESOURCE  = new Type(
        "eiam:event:app:delete_app_permission_resource", "删除应用权限资源", APP_RESOURCE,
        List.of(UserType.ADMIN));

    /**
     * 启用应用权限资源
     */
    public static Type ENABLE_APP_PERMISSION_RESOURCE  = new Type(
        "eiam:event:app:enable_app_permission_resource", "启用应用权限资源", APP_RESOURCE,
        List.of(UserType.ADMIN));

    /**
     * 禁用应用权限资源
     */
    public static Type DISABLE_APP_PERMISSION_RESOURCE = new Type(
        "eiam:event:app:disable_app_permission_resource", "禁用应用权限资源", APP_RESOURCE,
        List.of(UserType.ADMIN));

    /**
     * 删除用户身份提供商绑定
     */
    public static Type DELETE_USER_IDP_BIND            = new Type(
        "eiam:event:app:delete_user_idp_bind", "删除用户身份提供商绑定", APP_RESOURCE,
        List.of(UserType.ADMIN));

    /**
     * 添加应用角色
     */
    public static Type SAVE_APP_PERMISSION_ROLE        = new Type(
        "eiam:event:app:save_app_permission_role", "添加应用角色", APP_RESOURCE, List.of(UserType.ADMIN));

    /**
     * 修改应用角色
     */
    public static Type UPDATE_APP_PERMISSION_ROLE      = new Type(
        "eiam:event:app:update_app_permission_role", "修改应用角色", APP_RESOURCE,
        List.of(UserType.ADMIN));

    /**
     * 删除应用角色
     */
    public static Type DELETE_APP_PERMISSION_ROLE      = new Type(
        "eiam:event:app:delete_app_permission_role", "删除应用角色", APP_RESOURCE,
        List.of(UserType.ADMIN));

    /**
     * 添加应用分组
     */
    public static Type ADD_APP_GROUP                   = new Type("eiam:event:app_group:create",
        "添加应用分组", APP_GROUP_RESOURCE, List.of(UserType.ADMIN));
    /**
     * 编辑应用分组
     */
    public static Type UPDATE_APP_GROUP                = new Type("eiam:event:app_group:update",
        "修改应用分组", APP_GROUP_RESOURCE, List.of(UserType.ADMIN));

    /**
     * 删除应用分组
     */
    public static Type DELETE_APP_GROUP                = new Type("eiam:event:app_group:delete",
        "删除应用分组", APP_GROUP_RESOURCE, List.of(UserType.ADMIN));

    /**
     * 移除应用分组关联
     */
    public static Type REMOVE_APP_GROUP_ASSOCIATION    = new Type(
        "eiam:event:remove_app_group_association", "移除应用分组关联", APP_GROUP_RESOURCE,
        List.of(UserType.ADMIN));

}
