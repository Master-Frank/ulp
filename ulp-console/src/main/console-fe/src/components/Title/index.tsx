/*
 * ulp-console - United Login Platform
 * Copyright (c) 2022-Present Frank Zhang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import { Button } from 'antd';
import { history } from '@umijs/max';
import classnames from 'classnames';
import useStyle from './style';
import React from 'react';

const prefixCls = 'topiam-title-wrapper';
export type IProps = {
  title: string | React.ReactNode;
  aside?: any;
  size?: 'h1' | 'h2' | 'h3';
  hasBack?: boolean;
};

const fontsize = {
  h1: '30px',
  h2: '20px',
  h3: '16px',
};

export const Title = (props: IProps) => {
  const { aside = null, hasBack = false, title, size = 'h1' } = props;
  const { wrapSSR, hashId } = useStyle(prefixCls);

  const style = {
    fontSize: fontsize[size],
  };

  const onBack = () => {
    history.back();
  };
  return wrapSSR(
    <div className={classnames(`${prefixCls}`, hashId)}>
      <div className={classnames(`${prefixCls}-title`, hashId)} style={style}>
        <span>{title}</span>
        {hasBack && (
          <Button
            size="small"
            onClick={onBack}
            className={classnames(`${prefixCls}-title-back-btn`, hashId)}
          >
            返回
          </Button>
        )}
      </div>

      <div>{aside}</div>
    </div>,
  );
};
export default Title;
