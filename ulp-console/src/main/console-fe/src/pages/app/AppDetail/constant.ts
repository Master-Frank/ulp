/*
 * ulp-console - United Login Platform
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
/**
 * ConfigTabs
 */
export enum ConfigTabs {
  //应用配置
  app_config = 'app_config',
  //协议
  protocol_config = 'protocol_config',
  //访问授权
  access_policy = 'access_policy',
  //登录访问
  login_access = 'login_access',
  //应用账户
  app_account = 'app_account',
}

/**
 * SSO 授权范围
 */
export enum SsoScope {
  //手动授权
  AUTHORIZATION = 'authorization',
  //全员可访问
  ALL_ACCESS = 'all_access',
}

/**
 * 证书使用类型
 */
export enum CertUsingType {
  JWT_ENCRYPT = 'jwt_encrypt',
}

/**
 * 表单加密类型
 */
export enum FormEncryptType {
  /**aes*/
  aes = 'aes',
  /**base64*/
  base64 = 'base64',
  /**md5*/
  md5 = 'md5',
}
