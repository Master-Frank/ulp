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
 * 应用信息
 */
export type GetApp = {
  id: string;
  type: string;
  name: string;
  icon: string;
  template: string;
  protocol: string;
  protocolName: string;
  clientId: string;
  clientSecret: string;
  //sso登录链接
  initLoginUrl: string;
  nameIdValueType: string;
  //授权范围
  authorizationType: string;
  enabled: boolean;
  remark: string;
  groupIds: string[];
};
