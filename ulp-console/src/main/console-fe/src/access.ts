/*
 * eiam-console - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
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
