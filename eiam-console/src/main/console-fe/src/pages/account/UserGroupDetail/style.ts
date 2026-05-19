/*
 * eiam-console - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
import { createStyles } from 'antd-style';

const useStyle = createStyles(({ prefixCls }) => {
  const antCls = `.${prefixCls}`;
  return {
    descriptionRemark: {
      [`${antCls}-descriptions-item-container ${antCls}-space-item`]: {
        span: {
          padding: '0 !important',
        },
      },
    },
  };
});

export default useStyle;
