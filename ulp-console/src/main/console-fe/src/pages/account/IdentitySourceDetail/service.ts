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
 * 获取身份源
 */
export async function getIdentitySource(
  id: string,
): Promise<API.ApiResult<AccountAPI.GetIdentitySource>> {
  return request(`/api/v1/identity_source/get/${id}`);
}

/**
 * 获取身份源
 */
export async function getIdentitySourceConfig(
  id: string,
): Promise<API.ApiResult<AccountAPI.GetIdentitySourceConfig>> {
  return request(`/api/v1/identity_source/get/config/${id}`);
}

/**
 * 执行拉取身份源数据
 */
export async function executeIdentitySourceSync(id: string): Promise<API.ApiResult<boolean>> {
  return request(`/api/v1/identity_source/sync/execute/${id}`, { method: 'POST' });
}

/**
 * 修改身份源
 */
export async function updateIdentitySource(
  params: Record<any, any>,
): Promise<API.ApiResult<{ id: string }>> {
  return request(`/api/v1/identity_source/update`, {
    data: params,
    method: 'PUT',
    requestType: 'json',
  });
}

/**
 * 保存身份源配置
 */
export async function saveIdentitySourceConfig(
  config: Record<string, any>,
): Promise<API.ApiResult<boolean>> {
  return request(`/api/v1/identity_source/save/config`, {
    method: 'PUT',
    data: config,
    requestType: 'json',
  });
}

/**
 * 身份源配置验证
 */
export async function identitySourceConfigValidator(data: {
  provider: string;
  config: Record<string, string>;
}): Promise<API.ApiResult<boolean>> {
  return request(`/api/v1/identity_source/config_validator`, {
    method: 'POST',
    data: data,
    requestType: 'json',
  });
}

/**
 * 获取身份源历史列表
 */
export async function getIdentitySourceSyncHistoryList(
  params: Record<string, any>,
  sort: Record<string, SortOrder>,
  filter: Record<string, (string | number)[] | null>,
): Promise<RequestData<AccountAPI.ListIdentitySourceSyncHistory>> {
  return request(`/api/v1/identity_source/sync/history_list`, {
    params: { ...params, ...sortParamConverter(sort), ...filterParamConverter(filter) },
  }).then((result: API.ApiResult<AccountAPI.ListIdentitySourceSyncHistory>) => {
    const data: RequestData<AccountAPI.ListIdentitySourceSyncHistory> = {
      data: result?.result?.list ? result?.result?.list : [],
      success: result?.success,
      total: result?.result?.pagination ? result?.result?.pagination.total : 0,
    };
    return Promise.resolve(data);
  });
}

/**
 * 获取身份源记录列表
 */
export async function getIdentitySourceSyncRecordList(
  params: Record<string, any>,
  sort: Record<string, SortOrder>,
  filter: Record<string, (string | number)[] | null>,
): Promise<RequestData<AccountAPI.ListIdentitySourceSyncRecord>> {
  return request(`/api/v1/identity_source/sync/record_list`, {
    params: { ...params, ...sortParamConverter(sort), ...filterParamConverter(filter) },
  }).then((result: API.ApiResult<AccountAPI.ListIdentitySourceSyncRecord>) => {
    const data: RequestData<AccountAPI.ListIdentitySourceSyncRecord> = {
      data: result?.result?.list ? result?.result?.list : [],
      success: result?.success,
      total: result?.result?.pagination ? result?.result?.pagination.total : 0,
    };
    return Promise.resolve(data);
  });
}

/**
 * 获取身份源事件记录列表
 */
export async function getIdentitySourceEventRecordList(
  params: Record<string, any>,
  sort: Record<string, SortOrder>,
  filter: Record<string, (string | number)[] | null>,
): Promise<RequestData<AccountAPI.ListIdentitySourceEventRecord>> {
  return request(`/api/v1/identity_source/event/record_list`, {
    params: { ...params, ...sortParamConverter(sort), ...filterParamConverter(filter) },
  }).then((result: API.ApiResult<AccountAPI.ListIdentitySourceEventRecord>) => {
    const data: RequestData<AccountAPI.ListIdentitySourceEventRecord> = {
      data: result?.result?.list ? result?.result?.list : [],
      success: result?.success,
      total: result?.result?.pagination ? result?.result?.pagination.total : 0,
    };
    return Promise.resolve(data);
  });
}
