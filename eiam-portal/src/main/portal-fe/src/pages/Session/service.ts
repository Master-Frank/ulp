/*
 * eiam-portal - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
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
