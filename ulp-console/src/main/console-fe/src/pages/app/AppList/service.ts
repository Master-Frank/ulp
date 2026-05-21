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

/**
 * 启用应用
 */
export async function enableApp(id: string): Promise<API.ApiResult<boolean>> {
  return request(`/api/v1/app/enable/${id}`, { method: 'PUT' });
}

/**
 * 禁用应用
 */
export async function disableApp(id: string): Promise<API.ApiResult<boolean>> {
  return request(`/api/v1/app/disable/${id}`, { method: 'PUT' });
}
