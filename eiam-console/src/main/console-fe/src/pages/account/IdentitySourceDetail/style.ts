/*
 * eiam-console - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
import { createStyles } from 'antd-style';

const useStyles = createStyles(({ prefixCls }) => {
  return {
    step_form: {
      [`.${prefixCls}-form-item`]: {
        [`.${prefixCls}-form-item-control-input`]: {
          width: '100%',
        },
      },
    },
  };
});

export default useStyles;
