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
import { ListTemplate } from './data.d';
import { RequestData } from '@ant-design/pro-components';
import { filterParamConverter, sortParamConverter } from '@/utils/utils';
import { SortOrder } from 'antd/es/table/interface';

/**
 * 获取应用模板列表
 */
export async function getAppTemplateList(
  params: Record<string, any>,
  sort: Record<string, SortOrder>,
  filter: Record<string, (string | number)[] | null>,
): Promise<RequestData<ListTemplate>> {
  return request<API.ApiResult<ListTemplate>>('/api/v1/app/template/list', {
    params: { ...params, ...sortParamConverter(sort), ...filterParamConverter(filter) },
  }).then((result: API.ApiResult<ListTemplate>) => {
    const data: RequestData<ListTemplate> = {
      data: result ? result?.result : [],
      success: result?.success,
    };
    return Promise.resolve(data);
  });
}

/**
 * Create Application
 */
export async function createApp(
  params: Record<string, string>,
): Promise<API.ApiResult<Record<string, string>>> {
  return request<API.ApiResult<Record<string, string>>>(`/api/v1/app/create`, {
    method: 'POST',
    data: params,
    requestType: 'json',
  });
}
