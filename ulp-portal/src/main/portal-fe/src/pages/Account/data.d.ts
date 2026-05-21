/*
 * eiam-portal - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
/**
 * 账户信息
 */
export type AccountInfo = {
  /** 用户ID */
  id: string;
  avatar: string;
  username: string;
  phone: string;
  access: string;
};

export interface GetBoundIdpList {
  id: string;
  code: string;
  name: string;
  type: string;
  category: string;
  bound: boolean;
  idpId: string;
  authorizationUri: string;
}

/**
 * 账户菜单类型
 */
export enum AccountSettingsStateKey {
  base = 'base',
  security = 'security',
  bind = 'bind',
}
