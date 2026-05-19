/*
 * eiam-console - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
/**
 * 管理员列表
 */
export interface AdministratorList {
  id: string;
  username: string;
  fullName: string;
  avatar: string;
  email: string;
  phone: string;
  status: string;
  emailVerified: boolean;
  phoneVerified: boolean;
  authTotal: number;
  lastAuthIp: string;
  lastAuthTime: Date;
  initialized: boolean;
}
