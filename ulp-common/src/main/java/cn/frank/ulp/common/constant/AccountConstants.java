/*
 * ulp-common - United Login Platform
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
package cn.frank.ulp.common.constant;

import static cn.frank.ulp.support.constant.EiamConstants.ROOT_NODE;
import static cn.frank.ulp.support.constant.EiamConstants.V1_API_PATH;

/**
 * 账户常量
 *
 * @author Frank Zhang
 */
public final class AccountConstants {

    /**
     * 用户API
     */
    public final static String USER_PATH            = V1_API_PATH + "/user";
    /**
     * 组织机构API
     */
    public final static String ORGANIZATION_PATH    = V1_API_PATH + "/organization";
    /**
     * 用户组API
     */
    public final static String USER_GROUP_PATH      = V1_API_PATH + "/user_group";

    /**
     * 身份源API
     */
    public final static String IDENTITY_SOURCE_PATH = V1_API_PATH + "/identity_source";

    /**
     * 身份源缓存 cacheName
     */
    public static final String IDS_CACHE_NAME       = "ids";

    /**
     * user 缓存 cacheName
     */
    public static final String USER_CACHE_NAME      = "user";
    /**
     * org 缓存 cacheName
     */
    public static final String ORG_CACHE_NAME       = "organization";

    /**
     * 根部门 ID
     */
    public static final String ROOT_DEPT_ID         = ROOT_NODE;

}
