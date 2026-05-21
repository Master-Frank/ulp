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
import { filterParamConverter, sortParamConverter } from '@/utils/utils';
import type { RequestData } from '@ant-design/pro-components';
import type { SortOrder } from 'antd/es/table/interface';
import { request } from '@umijs/max';
import { AppGroupList, AppList } from './data.d';

/**
 * 获取应用列表
 */
export async function queryAppList(
  params: Record<string, any>,
  sort: Record<string, SortOrder>,
  filter: Record<string, (string | number)[] | null>,
): Promise<RequestData<AppList>> {
  const { result, success } = await request<API.ApiResult<AppList>>('/api/v1/app/list', {
    params: { ...params, ...sortParamConverter(sort), ...filterParamConverter(filter) },
  });
  return {
    data: result?.list ? result?.list : [],
    success: success,
    total: result?.pagination ? result?.pagination.total : 0,
  };
}

/**
 * 获取应用分组
 */
export async function getAppGroupList(): Promise<API.ApiResult<AppGroupList>> {
  return request<API.ApiResult<any>>('/api/v1/app/group_list', {});
}
