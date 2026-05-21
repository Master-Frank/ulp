/*
 * eiam-console - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
type BaseCreateUserProps = {
  visible: boolean;
  organization?: { id: string | number; name: string };
  onCancel: () => void;
  onFinish: (success: boolean, continued: boolean) => void;
};
export type CreateUserProps = BaseCreateUserProps;
