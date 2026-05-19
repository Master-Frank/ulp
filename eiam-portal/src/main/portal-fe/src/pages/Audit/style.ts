/*
 * eiam-portal - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
import { createStyles } from 'antd-style';

const useStyle = createStyles(({ prefixCls }) => {
  return {
    main: {
      [`.${prefixCls}-table-expanded-row-fixed`]: {
        width: 'auto !important',
      },
    },
  };
});
export default useStyle;
