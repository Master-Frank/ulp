/*
 * eiam-portal - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
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
