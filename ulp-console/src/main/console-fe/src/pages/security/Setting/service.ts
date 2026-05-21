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
import { request } from '@umijs/max';
import { BasicSettingConfig, SecurityDefensePolicyConfig } from './data.d';

/**
 * 获取基础配置
 */
export async function getBasicSettingConfig(): Promise<API.ApiResult<BasicSettingConfig>> {
  return request('/api/v1/setting/security/basic/config');
}

/**
 * 保存基础配置
 */
export async function saveBasicSettingConfig(
  params: BasicSettingConfig,
): Promise<API.ApiResult<boolean>> {
  return request('/api/v1/setting/security/basic/save', {
    method: 'POST',
    data: { ...params },
    requestType: 'form',
  });
}

/**
 * 获取内容安全策略配置
 */
export async function getSecurityDefensePolicyConfig(): Promise<
  API.ApiResult<SecurityDefensePolicyConfig>
> {
  return request('/api/v1/setting/security/defense_policy/config');
}

/**
 * 保存内容安全策略配置
 */
export async function saveSecurityDefensePolicyConfig(
  params: SecurityDefensePolicyConfig,
): Promise<API.ApiResult<boolean>> {
  return request('/api/v1/setting/security/defense_policy/save', {
    method: 'POST',
    data: { ...params },
    requestType: 'form',
  });
}
