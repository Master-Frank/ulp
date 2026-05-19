/*
 * eiam-console - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
import { createStyles } from 'antd-style';

const useStyle = createStyles(({ prefixCls }, props) => {
  const antCls = `.${prefixCls}`;
  const prefix = `${props}`;
  return {
    main: {
      [`.${prefix}`]: {
        [`&-table`]: {
          [`${antCls}-pro-card`]: {
            [`${antCls}-pro-card-body`]: {
              padding: '24px 0px 0px',
            },
          },
        },
      },
    },
  };
});
export default useStyle;
