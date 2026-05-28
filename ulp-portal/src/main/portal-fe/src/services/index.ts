/*
 * ulp-portal - United Login Platform
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
import { request } from '@@/plugin-request/request';

/**
 * 获取登录公钥
 */
export async function getLoginEncryptSecret(): Promise<API.ApiResult<API.EncryptSecret>> {
  return request(`/api/v1/public_secret?type=login`);
}

/**
 * 获取加密公钥
 */
export async function getEncryptSecret(): Promise<API.ApiResult<API.EncryptSecret>> {
  return request(`/api/v1/public_secret?type=encrypt`);
}

/**
 * 退出登录
 */
export async function outLogin() {
  return request('/api/v1/logout', {
    method: 'post',
  });
}

/**
 * 获取当前用户
 */
export async function getCurrentUser(): Promise<API.ApiResult<API.CurrentUser>> {
  return request<API.ApiResult<API.CurrentUser>>('/api/v1/session/current_user', {
    responseType: 'json',
  });
}

/**
 * 获取当前session状态
 */
export async function getCurrentStatus(): Promise<API.ApiResult<API.CurrentStatus>> {
  return request<API.ApiResult<API.CurrentStatus>>('/api/v1/session/current_status', {
    skipErrorHandler: true,
  });
}
