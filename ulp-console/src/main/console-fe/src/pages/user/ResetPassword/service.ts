/*
 * eiam-console - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
import { request } from '@umijs/max';

/**
 * 重置密码
 *
 * @param encrypt
 */
export async function resetPassword(encrypt: string): Promise<API.ApiResult<boolean>> {
  return request<API.ApiResult<boolean>>('/api/v1/reset_password', {
    method: 'POST',
    data: { encrypt: encrypt },
    skipErrorHandler: true,
  }).catch(({ response: { data } }) => {
    return data;
  });
}
