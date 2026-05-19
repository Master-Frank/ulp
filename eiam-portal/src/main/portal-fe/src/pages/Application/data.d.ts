/*
 * eiam-portal - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
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
