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

/**
 * 获取存储配置
 */
export async function getStorageConfig(): Promise<API.ApiResult<Record<string, any>>> {
  return request(`/api/v1/setting/storage/config`);
}

/**
 * 保存存储配置
 *
 * @param params
 */
export async function saveStorageConfig(
  params: Record<string, unknown>,
): Promise<API.ApiResult<boolean>> {
  return request(`/api/v1/setting/storage/save`, {
    data: { ...params },
    method: 'POST',
  });
}

/**
 * 禁用存储服务
 *
 */
export async function disableStorage(): Promise<API.ApiResult<Record<string, any>>> {
  return request(`/api/v1/setting/storage/disable`, {
    method: 'PUT',
  });
}

/**
 * 获取IP地理位置配置
 */
export async function getGeoIpConfig(): Promise<API.ApiResult<Record<string, any>>> {
  return request(`/api/v1/setting/geo_ip/config`);
}

/**
 * 保存IP地理位置配置
 *
 * @param params
 */
export async function saveGeoIpConfig(
  params: Record<string, unknown>,
): Promise<API.ApiResult<boolean>> {
  return request(`/api/v1/setting/geo_ip/save`, {
    data: { ...params },
    method: 'POST',
  });
}

/**
 * 关闭地理位置服务启用/禁用
 *
 */
export async function disableGeoIp(): Promise<API.ApiResult<Record<string, any>>> {
  return request(`/api/v1/setting/geo_ip/disable`, {
    method: 'PUT',
  });
}
