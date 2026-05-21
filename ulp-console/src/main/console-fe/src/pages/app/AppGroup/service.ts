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
 * 创建应用分组
 */
export async function createAppGroup(
  params: Record<string, string>,
): Promise<API.ApiResult<Record<string, string>>> {
  return request<API.ApiResult<Record<string, string>>>(`/api/v1/app/group/create`, {
    method: 'POST',
    data: params,
    requestType: 'json',
  });
}

/**
 * 获取应用分组
 */
export async function getAppGroup(id: string): Promise<API.ApiResult<Record<string, string>>> {
  return request<API.ApiResult<Record<string, string>>>(`/api/v1/app/group/get/${id}`, {
    method: 'GET',
  });
}

/**
 * 修改应用分组
 */
export async function updateAppGroup(
  params: Record<string, string>,
): Promise<API.ApiResult<Record<string, string>>> {
  return request<API.ApiResult<Record<string, string>>>(`/api/v1/app/group/update`, {
    method: 'PUT',
    data: params,
    requestType: 'json',
  });
}

/**
 * Remove App Group
 */
export async function removeAppGroup(id: string): Promise<API.ApiResult<boolean>> {
  return request<API.ApiResult<boolean>>(`/api/v1/app/group/delete/${id}`, {
    method: 'DELETE',
  });
}
