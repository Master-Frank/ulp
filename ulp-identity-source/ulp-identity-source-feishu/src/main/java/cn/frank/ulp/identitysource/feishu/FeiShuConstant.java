/*
 * ulp-identity-source-feishu - United Login Platform
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
package cn.frank.ulp.identitysource.feishu;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import static cn.frank.ulp.support.constant.EiamConstants.COLON;

/**
 * 常量
 *
 * @author TopIAM
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FeiShuConstant {

    public static final String LOGGER_NAME          = "feishu-identity";

    /**
     * 分页大小， 最大50
     */
    public static final int    PAGE_SIZE            = 50;
    /**
     * 钉钉开放接口域名
     */
    public static final String DOMAIN               = "https://open.feishu.cn";

    /**
     * 获取 app_access_token（企业自建应用）
     * https://open.feishu.cn/document/ukTMukTMukTM/ukDNz4SO0MjL5QzM/auth-v3/auth/app_access_token_internal
     */
    public static final String APP_ACCESS_TOKEN_URL = DOMAIN
                                                      + "/open-apis/auth/v3/app_access_token/internal";

    /**
     * 获取 tenant_access_token（企业自建应用）
     * https://open.feishu.cn/document/ukTMukTMukTM/ukDNz4SO0MjL5QzM/auth-v3/auth/tenant_access_token_internal
     */
    public static final String ACCESS_TOKEN_URL     = DOMAIN
                                                      + "/open-apis/auth/v3/tenant_access_token/internal";
    /**
     * 递归获取子部门列表
     * https://open.feishu.cn/document/uAjLw4CM/ukTMukTMukTM/reference/contact-v3/department/children
     */
    public static final String DEPARTMENT_LIST      = DOMAIN
                                                      + "/open-apis/contact/v3/departments/%s/children";
    /**
     * 获取单个部门信息
     * https://open.feishu.cn/document/uAjLw4CM/ukTMukTMukTM/reference/contact-v3/department/get
     */
    public static final String DEPARTMENT_GET       = DOMAIN + "/open-apis/contact/v3/departments/";
    /**
     * 获取部门直属用户列表
     * https://open.feishu.cn/document/uAjLw4CM/ukTMukTMukTM/reference/contact-v3/user/find_by_department
     */
    public static final String USER_LIST            = DOMAIN
                                                      + "/open-apis/contact/v3/users/find_by_department";
    /**
     * 获取用户信息
     * https://open.feishu.cn/document/uAjLw4CM/ukTMukTMukTM/reference/contact-v3/user/get
     */
    public static final String GET_USER             = DOMAIN + "/open-apis/contact/v3/users/";
    /**
     * accessToken缓存名
     */
    public static final String FEI_SHU_ACCESS_TOKEN = "fei_shu_access_token" + COLON;
}
