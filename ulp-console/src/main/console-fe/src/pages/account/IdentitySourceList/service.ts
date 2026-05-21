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
import { SortOrder } from 'antd/es/table/interface';
import { RequestData } from '@ant-design/pro-components';
import { request } from '@@/exports';
import { filterParamConverter, sortParamConverter } from '@/utils/utils';

/**
 * 获取身份源平台列表
 */
export async function getIdentityProviderList(
  params: Record<string, any>,
  sort: Record<string, SortOrder>,
  filter: Record<string, (string | number)[] | null>,
): Promise<RequestData<AccountAPI.ListIdentitySource>> {
  return request<API.ApiResult<AccountAPI.ListIdentitySource>>('/api/v1/identity_source/list', {
    params: { ...params, ...sortParamConverter(sort), ...filterParamConverter(filter) },
  }).then((result: API.ApiResult<AccountAPI.ListIdentitySource>) => {
    const data: RequestData<AccountAPI.ListIdentitySource> = {
      data: result?.result?.list ? result?.result?.list : [],
      success: result?.success,
      total: result?.result?.pagination ? result?.result?.pagination.total : 0,
    };
    return Promise.resolve(data);
  });
}

/**
 * 创建身份源
 */
export async function createIdentitySource(
  params: Record<string, any>,
): Promise<API.ApiResult<{ id: string }>> {
  return request(`/api/v1/identity_source/create`, {
    data: params,
    method: 'POST',
    requestType: 'json',
  });
}

/**
 * 启用身份源
 */
export async function enableIdentitySource(id: string): Promise<API.ApiResult<boolean>> {
  return request(`/api/v1/identity_source/enable/${id}`, { method: 'PUT' });
}

/**
 * 禁用身份源
 */
export async function disableIdentitySource(id: string): Promise<API.ApiResult<boolean>> {
  return request(`/api/v1/identity_source/disable/${id}`, { method: 'PUT' });
}
