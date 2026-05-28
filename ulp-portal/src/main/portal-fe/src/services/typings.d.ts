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
declare namespace API {
  /**
   * API Result
   */
  export type ApiResult<T> = {
    /** 状态码 */
    status: string;
    /** 成功 */
    success: boolean;
    /** 消息结果 */
    message: string;
    /** 结果 */
    result?: PaginationResult<T> & T & T[];
  } & Record<string, any>;

  /**
   * 分页结果
   */
  export type PaginationResult<T> = {
    /** List */
    list: T[];
    /** 分页 */
    pagination: Pagination;
  };

  /**
   * 分页
   */
  export type Pagination = {
    /** 总条数 */
    total: number;
    /** 总页数 */
    totalPages: number;
    /** 当前页 */
    current: number;
  };

  /**
   * 当前用户
   */
  export type CurrentUser = {
    /** 用户ID */
    id: string;
    avatar: string;
    username: string;
    fullName: string;
    nickName: string;
    phone: string;
    email: string;
    totpBind: boolean;
  };

  /**
   * 当前状态
   */
  export type CurrentStatus = {
    /** 用户ID */
    id: string;
    authenticated: boolean;
    status?: string;
  };

  /**
   * 加密秘钥
   */
  export type EncryptSecret = {
    secret: string;
  };
}
