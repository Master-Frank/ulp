/*
 * eiam-console - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
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
