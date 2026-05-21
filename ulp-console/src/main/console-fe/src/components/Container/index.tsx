/*
 * eiam-console - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
import classnames from 'classnames';
import useStyle from './style';

type IProps = {
  children: any;
  maxWidth?: number | string;
};
const prefixCls = 'topiam-container';

export const Container = (props: IProps) => {
  const { children = null, maxWidth = 1000 } = props;
  const { wrapSSR, hashId } = useStyle(prefixCls);
  return wrapSSR(
    <div className={classnames(`${prefixCls}`, hashId)} style={{ maxWidth }}>
      {children}
    </div>,
  );
};
