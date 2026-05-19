/*
 * eiam-console - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
import { DataNode } from 'antd/es/tree';

export type CreateOrganizationFormProps<T> = {
  visible: boolean;
  onCancel?: () => void;
  onFinish: (formData: Partial<T>) => Promise<boolean | void>;
  currentNode?: DataNode | any;
};
