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
export type AppList = {
  id: string;
  type: string;
  protocol: string;
  template: string;
  icon?: string;
  name: string;
  initLoginType: InitLoginType | string;
  initLoginUrl: string;
  description: string;
};

export enum InitLoginType {
  /**
   * 仅允许应用发起 SSO
   */
  only_app_init_sso = 'only_app_init_sso',
  /**
   * 门户或应用发起 SSO
   */
  portal_or_app_init_sso = 'portal_or_app_init_sso',
}

export type AppGroupList = {
  id: string;
  name: string;
  appCount: number;
};
