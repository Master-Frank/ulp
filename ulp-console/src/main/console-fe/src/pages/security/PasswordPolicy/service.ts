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
import { PasswordPolicyConfig, WeakPasswordLib } from './data.d';
import { request } from '@umijs/max';

/**
 * 查询系统弱密码库
 */
export async function getWeakPasswordLib(): Promise<API.ApiResult<WeakPasswordLib[]>> {
  return request('/api/v1/setting/security/password_policy/weak_password_lib');
}

/**
 * 获取密码策略配置
 */
export async function getPasswordPolicyConfig(): Promise<API.ApiResult<PasswordPolicyConfig>> {
  return request('/api/v1/setting/security/password_policy/config');
}

/**
 * 保存密码策略配置
 */
export async function savePasswordPolicyConfig(
  params: Record<string, any>,
): Promise<API.ApiResult<boolean>> {
  return request('/api/v1/setting/security/password_policy/save', {
    method: 'POST',
    data: { ...params },
    requestType: 'form',
  });
}
