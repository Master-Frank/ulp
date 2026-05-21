/*
 * eiam-console - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
import { Key } from 'rc-tree/lib/interface';

type UpdateOrganizationFormProps<T> = {
  /**
   * 是否显示
   */
  visible: boolean;
  /**
   * 取消方法
   */
  onCancel?: () => void;
  onFinish: (formData: T) => Promise<boolean | void>;
  currentNode?: Key;
};
