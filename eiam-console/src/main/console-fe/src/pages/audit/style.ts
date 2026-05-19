/*
 * eiam-console - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
import { createStyles } from 'antd-style';

const useStyles = createStyles(({ prefixCls }) => ({
  expandedRow: {
    [`.${prefixCls}-table-expanded-row-fixed`]: {
      width: 'auto !important',
    },
  },
}));
export default useStyles;
