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
/**
 * 审计列表
 */
export type AuditList = {
  id: string;
  username: string;
  context: string;
  targets: Record<string, any>;
  userId: string;
  userAgent: Record<string, string>;
  geoLocation: Record<string, string>;
  eventType: string;
  eventResult: string;
  eventTime: string;
  eventStatus: 'success' | 'fail';
};
/**
 * 审计类型分组
 */
export type AuditTypeGroup = {
  name: string;
  code: string;
  types: AuditType[];
};

/**
 * 审计类型
 */
export type AuditType = {
  name: string;
  code: string;
};

export enum UserType {
  admin = 'admin',
  user = 'user',
}

export enum EventStatus {
  success = 'success',
  fail = 'fail',
}
