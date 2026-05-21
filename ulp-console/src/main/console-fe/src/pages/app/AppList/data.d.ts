/*
 * eiam-console - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
/**
 * 应用列表
 */
export type AppList = {
  id: string;
  name: string;
  icon?: string;
  protocol: string;
  enabled: boolean;
  type: string;
  template: string;
  remark: string;
};
