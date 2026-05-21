/*
 * eiam-console - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
import { request } from '@umijs/max';

/**
 * 启用应用
 */
export async function enableApp(id: string): Promise<API.ApiResult<boolean>> {
  return request(`/api/v1/app/enable/${id}`, { method: 'PUT' });
}

/**
 * 禁用应用
 */
export async function disableApp(id: string): Promise<API.ApiResult<boolean>> {
  return request(`/api/v1/app/disable/${id}`, { method: 'PUT' });
}
