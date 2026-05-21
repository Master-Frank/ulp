/*
 * eiam-console - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
import type { RcFile } from 'antd/es/upload';
import { request } from '@umijs/max';

/**
 * 上传文件
 * @param file
 * @param fileName
 * @param onUploadProgress
 * @returns
 */
export async function uploadFile(
  file: string | RcFile | Blob,
  fileName?: string,
  onUploadProgress?: (progressEvent: ProgressEvent) => void,
): Promise<API.ApiResult<string>> {
  const formData = new FormData();
  formData.append('file', file);
  if (fileName) formData.append('fileName', fileName);
  return request('/api/v1/storage/upload', { method: 'POST', data: formData, onUploadProgress });
}
