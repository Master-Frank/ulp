/*
 * eiam-console - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
export type SecurityDefensePolicyConfig = {
  contentSecurityPolicy: string;
};

/**
 * 安全基础配置
 */
export type BasicSettingConfig = {
  frequentRegisterCheck: boolean;
  emailVerifiedDefault: boolean;
  sendWelcomeEmail: boolean;
  verifyOldEmail: boolean;
  verifyOldPhone: boolean;
};
