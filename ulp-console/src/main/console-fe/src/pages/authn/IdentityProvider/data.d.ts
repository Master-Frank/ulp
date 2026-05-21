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
 * 认证源
 */
export type ListIdentityProvider = {
  id: string;
  name: string;
  desc: string;
  remark: string;
  type: any;
  enabled: boolean;
  displayed: boolean;
};

/**
 * 认证源详情
 */
export type GetIdentityProvider = {
  type: string;
  redirectUri: string;
  config: any;
};
