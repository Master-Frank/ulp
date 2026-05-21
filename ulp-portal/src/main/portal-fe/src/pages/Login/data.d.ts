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
import type { IDP_TYPE } from '@/constants';

/**
 * 登录参数类型
 */
export type LoginParamsType = {
  username?: string;
  password?: string;
  phone?: string;
  recipient?: string;
  code?: string;
  type?: IDP_TYPE | string;
  'remember-me'?: boolean;
  redirect_uri?: string;
};

/**
 * Idp 列表
 */
export type IdpList = {
  code: string;
  name: string;
  type: string;
  category: string;
  authorizationUri: string;
};

export type LoginConfig = {
  idps: IdpList[];
};
