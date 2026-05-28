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
import { filterParamConverter, sortParamConverter } from '@/utils/utils';
import type { RequestData } from '@ant-design/pro-components';
import type { SortOrder } from 'antd/es/table/interface';
import { request } from '@umijs/max';
import { GetIdentityProvider, ListIdentityProvider } from './data.d';

/**
 * 获取身份提供商平台列表
 */
export async function getIdpList(
  params: Record<string, any>,
  sort: Record<string, SortOrder>,
  filter: Record<string, (string | number)[] | null>,
): Promise<RequestData<ListIdentityProvider>> {
  return request<API.ApiResult<ListIdentityProvider>>('/api/v1/authn/idp/list', {
    params: { ...params, ...sortParamConverter(sort), ...filterParamConverter(filter) },
  }).then((result: API.ApiResult<ListIdentityProvider>) => {
    const data: RequestData<ListIdentityProvider> = {
      data: result?.result?.list ? result?.result?.list : [],
      success: result?.success,
      total: result?.result?.pagination ? result?.result?.pagination.total : 0,
    };
    return Promise.resolve(data);
  });
}

/**
 * 获取身份提供商平台详情
 */
export async function getIdentityProvider(id: string): Promise<API.ApiResult<GetIdentityProvider>> {
  return request(`/api/v1/authn/idp/get/${id}`);
}

/**
 * 修改身份提供商
 */
export async function updateIdentityProvider(
  params: Record<any, any>,
): Promise<API.ApiResult<{ id: string }>> {
  return request(`/api/v1/authn/idp/update`, {
    data: params,
    method: 'PUT',
    requestType: 'json',
  });
}

/**
 * 保存身份提供商
 */
export async function createIdentityProvider(
  params: Record<string, any>,
): Promise<API.ApiResult<Record<string, any>>> {
  return request(`/api/v1/authn/idp/create`, {
    data: params,
    method: 'POST',
    requestType: 'json',
  });
}

/**
 * 启用身份提供商
 */
export async function enableIdentityProvider(id: string): Promise<API.ApiResult<boolean>> {
  return request(`/api/v1/authn/idp/enable/${id}`, { method: 'PUT' });
}

/**
 * 禁用身份提供商
 */
export async function disableIdentityProvider(id: string): Promise<API.ApiResult<boolean>> {
  return request(`/api/v1/authn/idp/disable/${id}`, { method: 'PUT' });
}

/**
 * 删除身份源
 */
export async function removeIdentityProvider(id: string): Promise<API.ApiResult<boolean>> {
  return request(`/api/v1/authn/idp/delete/${id}`, { method: 'DELETE' });
}
