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
import { request } from '@umijs/max';
import type { LoginConfig, LoginParamsType } from './data.d';
import { stringify } from 'querystring';

/**
 * 账户登录
 *
 * @param params
 */
export async function accountLogin(params: LoginParamsType) {
  return request<API.ApiResult<string>>('/api/v1/login', {
    method: 'POST',
    data: stringify(params),
    skipErrorHandler: true,
  }).catch(({ response: { data } }) => {
    return data;
  });
}

/**
 * 验证码登录
 *
 * @param isMail
 * @param params
 */
export async function otpLogin(isMail: boolean, params: LoginParamsType) {
  return request<API.ApiResult<string>>(`/api/v1/login/otp/${isMail ? 'mail' : 'sms'}`, {
    method: 'POST',
    data: stringify(params),
    skipErrorHandler: true,
  }).catch(({ response: { data } }) => {
    return data;
  });
}

/**
 * 发送验证码
 *
 */
export async function sendLoginCaptchaOpt(
  isMail: boolean,
  recipient: string,
): Promise<API.ApiResult<boolean>> {
  return request(`/api/v1/login/${isMail ? 'mail' : 'sms'}/send`, {
    method: 'POST',
    data: stringify({ recipient: recipient }),
    skipErrorHandler: true,
  }).catch(({ response: { data } }) => {
    return data;
  });
}

/**
 *  Idp绑定用户
 */
export async function idpBindUser(params: Record<string, any>): Promise<API.ApiResult<boolean>> {
  return request('/api/v1/login/idp_bind_user', {
    method: 'POST',
    data: stringify(params),
    skipErrorHandler: true,
  }).catch(({ response: { data } }) => {
    return data;
  });
}

/**
 * 获取登录配置
 */
export async function getLoginConfig(appId?: string): Promise<API.ApiResult<LoginConfig>> {
  return request<API.ApiResult<LoginConfig>>('/api/v1/login/config', { params: { appId } });
}

/**
 * 忘记密码发送验证码
 */
export async function forgetPasswordCode(recipient: string): Promise<API.ApiResult<any>> {
  return request<API.ApiResult<any>>('/api/v1/account/forget_password_code', {
    params: { recipient },
    skipErrorHandler: true,
  }).catch(({ response }) => response.data);
}

/**
 * 忘记密码预认证
 */
export async function prepareForgetPassword(encrypt: string): Promise<API.ApiResult<any>> {
  return request<API.ApiResult<any>>('/api/v1/account/prepare_forget_password', {
    data: { encrypt: encrypt },
    method: 'POST',
  }).catch(({ response }) => response.data);
}

/**
 * 忘记密码
 */
export async function forgetPassword(encrypt: string): Promise<API.ApiResult<any>> {
  return request<API.ApiResult<any>>('/api/v1/account/forget_password', {
    data: { encrypt: encrypt },
    method: 'PUT',
  }).catch(({ response }) => response.data);
}
