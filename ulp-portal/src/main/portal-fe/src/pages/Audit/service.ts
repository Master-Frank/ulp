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
import type { AuditList, AuditTypeGroup } from './data.d';

/**
 * 获取审计列表
 */
export async function getAuditList(
  params: Record<string, any>,
  sort: Record<string, SortOrder>,
  filter: Record<string, (string | number)[] | null>,
): Promise<RequestData<AuditList>> {
  return request<API.ApiResult<AuditList>>(`/api/v1/audit/list`, {
    params: {
      ...params,
      userType: 'user',
      startEventTime: params.eventTime && params.eventTime[0],
      endEventTime: params.eventTime && params.eventTime[1],
      ...sortParamConverter(sort),
      ...filterParamConverter(filter),
    },
  }).then((result: API.ApiResult<AuditList>) => {
    const data: RequestData<AuditList> = {
      data: result?.result?.list ? result?.result?.list : [],
      success: result?.success,
      total: result?.result?.pagination ? result?.result?.pagination.total : 0,
    };
    return Promise.resolve(data);
  });
}

/**
 * 查询审计字典
 */
export async function getAuditTypes(): Promise<API.ApiResult<AuditTypeGroup>> {
  return request(`/api/v1/audit/types/user`);
}
