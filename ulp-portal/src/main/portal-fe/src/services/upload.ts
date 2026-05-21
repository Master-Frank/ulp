/*
 * eiam-portal - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
import type { RcFile } from 'antd/es/upload';
import { request } from '@umijs/max';

/**
 * 上传文件
 * @param file
 * @param fileName
 * @returns
 */
export async function uploadFile(
  file: string | RcFile | Blob,
  fileName?: string,
): Promise<API.ApiResult<string>> {
  const formData = new FormData();
  formData.append('file', file);
  if (fileName) formData.append('fileName', fileName);
  return request('/api/v1/storage/upload', { method: 'POST', data: formData });
}
