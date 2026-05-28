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
/**
 * 邮件列表
 */
export type EmailTemplateList = {
  /** 名称 */
  name: string;
  /** 编码 */
  code: string;
  /** 内容 */
  content: string;
  /** 自定义*/
  custom: boolean;
  /** 描述 */
  description: string;
};

/**
 * 邮件详情
 */
export type GetEmailTemplate = {
  /** 名称 */
  name: string;
  /** 模板 */
  theme: string;
  /** 内容 */
  content: string;
  /** 描述 */
  desc: string;
  /** 自定义 */
  custom: boolean;
};

/**
 * 短信列表
 */
export type SmsTemplateList = {
  /** 名称 */
  name: string;
  /** 类型 */
  type: string;
  /** 内容 */
  content: string;
};
