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
// src/access.ts
export default function (initialState: { currentUser: API.CurrentUser }) {
  const { currentUser } = initialState;
  function actionFilter(action: string | string[]) {
    const actions: string[] = (currentUser && currentUser.access) || [];
    if (typeof action === 'string') {
      return actions?.includes(action);
    }
    return actions
      .map((i) => {
        return action?.includes(i);
      })
      .includes(true);
  }
  return {
    /* 操作项过滤器 */
    actionFilter: actionFilter,
  };
}
