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
import { request } from '@umijs/max';
import { EmailTemplateList, GetEmailTemplate, SmsTemplateList } from './data.d';

/**
 * 获取电子邮件模板列表
 *
 * @param params
 */
export async function getMailTemplateList(
  params: Record<string, any>,
): Promise<API.ApiResult<EmailTemplateList>> {
  return request('/api/v1/setting/mail_template/list', { params });
}

/**
 * 获取电子邮件模板详情
 *
 * @param params
 */
export async function getMailTemplate(params: {
  type: string;
}): Promise<API.ApiResult<GetEmailTemplate>> {
  return request(`/api/v1/setting/mail_template/${params.type}`, {});
}

/**
 * 获取短信模板列表
 *
 * @param params
 */
export async function getSmsTemplateList(
  params: Record<string, any>,
): Promise<API.ApiResult<SmsTemplateList>> {
  return request('/api/v1/setting/sms_template/list', { params });
}

/**
 * 禁用自定义邮件模块
 *
 */
export async function disableCustomTemplate(
  type: string,
): Promise<API.ApiResult<Record<string, any>>> {
  return request(`/api/v1/setting/mail_template/disable_custom/${type}`, {
    method: 'PUT',
  });
}

/**
 * 保存电子邮件模板
 *
 * @param params
 */
export async function saveMailTemplate(params: {
  type: string;
  content: string;
  theme: string;
  sender: string;
}): Promise<API.ApiResult<boolean>> {
  return request(`/api/v1/setting/mail_template/save_custom/${params.type}`, {
    data: { ...params },
    requestType: 'form',
    method: 'PUT',
  });
}

/**
 * 禁用邮件提供商
 */
export async function disableMailProvider(): Promise<API.ApiResult<boolean>> {
  return request(`/api/v1/setting/message/mail_provider/disable`, {
    method: 'PUT',
  });
}

/**
 * 获取邮件服务商配置
 */
export async function getMailProviderConfig(): Promise<API.ApiResult<Record<string, any>>> {
  return request(`/api/v1/setting/message/mail_provider/config`);
}

/**
 * 保存邮件服务商配置
 *
 * @param params
 */
export async function saveMailProvider(
  params: Record<string, any>,
): Promise<API.ApiResult<boolean>> {
  return request(`/api/v1/setting/message/mail_provider/save`, {
    data: { ...params },
    method: 'POST',
  });
}

/**
 * 邮件发送测试
 *
 * @param params
 */
export async function mailTest(
  params: Record<string, any>,
): Promise<API.ApiResult<Record<string, any>>> {
  return request(`/api/v1/setting/message/mail_provider/test`, { params });
}

/**
 * 保存短信服务商配置
 *
 * @param params
 */
export async function saveSmsProviderConfig(
  params: Record<string, unknown>,
): Promise<API.ApiResult<boolean>> {
  return request(`/api/v1/setting/message/sms_provider/save`, {
    data: { ...params },
    method: 'POST',
  });
}

/**
 * 禁用短信验证服务
 */
export async function disableSmsProvider(): Promise<API.ApiResult<boolean>> {
  return request(`/api/v1/setting/message/sms_provider/disable`, {
    method: 'PUT',
  });
}

/**
 * 获取短信服务商配置
 */
export async function getSmsProviderConfig(): Promise<API.ApiResult<Record<string, any>>> {
  return request(`/api/v1/setting/message/sms_provider/config`);
}

/**
 * 短信发送测试
 *
 * @param params
 */
export async function smsTest(
  params: Record<string, any>,
): Promise<API.ApiResult<Record<string, any>>> {
  return request(`/api/v1/setting/message/sms_provider/test`, { params });
}
