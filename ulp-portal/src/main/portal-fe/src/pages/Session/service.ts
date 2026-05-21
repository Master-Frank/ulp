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
import type { RequestData } from '@ant-design/pro-components';
import { request } from '@umijs/max';
import type { SessionList } from './data.d';

/**
 * 获取在线用户列表
 */
export async function getSessions(
  params: Record<string, string>,
): Promise<RequestData<SessionList>> {
  return request<API.ApiResult<SessionList>>('/api/v1/session/list', {
    method: 'get',
    params,
  }).then((result) => {
    const data: RequestData<SessionList> = {
      data: result?.result ? result?.result : [],
      success: result?.success,
    };
    return Promise.resolve(data);
  });
}

/**
 * 下线session
 */
export async function removeSessions(sessionIds: string): Promise<API.ApiResult<boolean>> {
  return request<API.ApiResult<boolean>>('/api/v1/session/remove', {
    method: 'DELETE',
    params: { sessionIds },
  });
}
