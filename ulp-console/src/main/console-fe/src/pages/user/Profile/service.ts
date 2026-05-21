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
import { request } from '@@/plugin-request/request';

/**
 * 准备修改手机号
 *
 * @param data
 */
export async function prepareChangePhone(data: {
  phone: string;
  phoneRegion: string;
  password: string;
}): Promise<API.ApiResult<boolean>> {
  return request(`/api/v1/user/profile/prepare_change_phone`, {
    data: data,
    method: 'POST',
    skipErrorHandler: true,
  }).catch(({ response: { data } }) => {
    return data;
  });
}

/**
 * 更换手机
 *
 * @param data
 */
export async function changePhone(data: Record<string, string>): Promise<API.ApiResult<boolean>> {
  return request(`/api/v1/user/profile/change_phone`, {
    data: data,
    method: 'PUT',
  });
}

/**
 * 准备更换邮箱
 *
 * @param data
 */
export async function prepareChangeEmail(data: {
  password: string;
  email: string;
}): Promise<API.ApiResult<boolean>> {
  return request(`/api/v1/user/profile/prepare_change_email`, {
    data: data,
    method: 'POST',
    skipErrorHandler: true,
  }).catch(({ response: { data } }) => {
    return data;
  });
}

/**
 * 更换邮箱
 *
 * @param data
 */
export async function changeEmail(data: Record<string, string>): Promise<API.ApiResult<boolean>> {
  return request(`/api/v1/user/profile/change_email`, {
    data: data,
    method: 'PUT',
  });
}

/**
 * 更换密码
 *
 * @param data
 */
export async function changePassword(data: {
  oldPassword: string;
  newPassword: string;
}): Promise<API.ApiResult<boolean>> {
  return request(`/api/v1/user/profile/change_password`, {
    data: data,
    method: 'PUT',
    skipErrorHandler: true,
  }).catch(({ response: { data } }) => {
    return data;
  });
}

/**
 * 更改基础信息
 *
 * @param data
 */
export async function changeBaseInfo(
  data: Record<string, string | undefined>,
): Promise<API.ApiResult<boolean>> {
  return request(`/api/v1/user/profile/change_info`, {
    data: data,
    method: 'PUT',
  });
}
