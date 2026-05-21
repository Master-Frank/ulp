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

/**
 * 配置Bean名称常量
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/3/23 22:53
 */
public final class ConfigBeanNameConstants {
    /**
     * 任务执行者
     */
    public static final String TASK_EXECUTOR                       = "taskExecutor";
    /**
     * 安全过滤器链
     */
    public static final String DEFAULT_SECURITY_FILTER_CHAIN       = "defaultSecurityFilterChain";
    public static final String IDP_SECURITY_FILTER_CHAIN           = "idpSecurityFilterChain";
    public static final String OIDC_PROTOCOL_SECURITY_FILTER_CHAIN = "oidcProtocolSecurityFilterChain";
    public static final String FORM_PROTOCOL_SECURITY_FILTER_CHAIN = "formProtocolSecurityFilterChain";
    public static final String JWT_PROTOCOL_SECURITY_FILTER_CHAIN  = "jwtProtocolSecurityFilterChain";

    /**
     * 默认密码策略管理器
     */
    public static final String DEFAULT_PASSWORD_POLICY_MANAGER     = "defaultPasswordPolicyManager";

    /**
     * 地理位置
     */
    public static final String GEO_LOCATION                        = "geoLocation";
    /**
     * 短信提供商发送
     */
    public static final String SMS_PROVIDER_SEND                   = "smsProviderSend";
    /**
     * 邮件提供商
     */
    public static final String MAIL_PROVIDER_SEND                  = "mailProviderSend";

}
