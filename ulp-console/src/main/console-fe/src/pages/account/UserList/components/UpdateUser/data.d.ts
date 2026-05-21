/*
 * eiam-console - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
export type UpdateFormProps = {
  id: string;
  visible: boolean;
  onCancel: () => void;
  afterClose: () => void;
  onFinish: (success: boolean) => void;
};
