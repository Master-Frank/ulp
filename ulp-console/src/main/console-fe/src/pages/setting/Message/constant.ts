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
export enum TabType {
  mail = 'mail',
  mail_template = 'mail_template',
  sms = 'sms',
}

export enum OssProvider {
  ALIYUN_OSS = 'aliyun_oss',
  TENCENT_COS = 'tencent_cos',
  QINIU_KODO = 'qiniu_kodo',
  LOCAL = 'local',
  MINIO = 'minio',
}
export enum Language {
  ZH = 'zh',
  EN = 'en',
}

export enum Region {
  BEIJING = 'ap-beijing',
  GUANGZHOU = 'ap-guangzhou',
  NANJING = 'ap-nanjing',
}
