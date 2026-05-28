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
export enum IdentitySourceDetailTabs {
  config = 'config',
  sync_history = 'sync-history',
  event_record = 'event-record',
}

/**
 * Mode
 */
export enum JobMode {
  /**周期*/
  period = 'period',
  /**定时*/
  timed = 'timed',
}

export const ConfigFormLayout = {
  labelCol: {
    xs: { span: 24 },
    sm: { span: 7 },
    md: { span: 6 },
    lg: { span: 5 },
    xl: { span: 7 },
  },
  wrapperCol: {
    xs: { span: 24 },
    sm: { span: 12 },
    md: { span: 13 },
    lg: { span: 14 },
    xl: { span: 12 },
  },
};

/**
 * 基础配置from参数
 */
export const BASIC_CONFIG_FROM_PARAM = {
  appId: ['basicConfig', 'appId'],
  appKey: ['basicConfig', 'appKey'],
  corpId: ['basicConfig', 'corpId'],
  appSecret: ['basicConfig', 'appSecret'],
  secret: ['basicConfig', 'secret'],
  callbackUrl: ['basicConfig', 'callbackUrl'],
  protocol: ['basicConfig', 'protocol'],
  ip: ['basicConfig', 'ip'],
  port: ['basicConfig', 'port'],
  administratorUsername: ['basicConfig', 'administratorUsername'],
  administratorPassword: ['basicConfig', 'administratorPassword'],
  baseDn: ['basicConfig', 'baseDn'],
  userObjectClass: ['basicConfig', 'userObjectClass'],
  orgObjectClass: ['basicConfig', 'orgObjectClass'],
};
