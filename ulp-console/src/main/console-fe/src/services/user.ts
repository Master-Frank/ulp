/*
 * eiam-console - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
import { request } from '@umijs/max';

/**
 * 获取当前用户
 */
export async function getCurrent(): Promise<API.ApiResult<API.CurrentUser>> {
  return request<API.ApiResult<API.CurrentUser>>('/api/v1/session/current_user');
}
