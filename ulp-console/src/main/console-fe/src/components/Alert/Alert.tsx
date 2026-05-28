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
import { Alert, AlertProps } from 'antd';
import React from 'react';
import { omit } from 'lodash';
import { createStyles } from 'antd-style';

export interface Props extends Omit<AlertProps, 'type'> {
  type?: 'success' | 'info' | 'warning' | 'error' | 'grey';
}

const useStyles = createStyles(({ prefixCls }) => {
  return {
    greyIcon: {
      [`.${prefixCls}-alert-icon`]: {
        color: 'rgb(107, 119, 133) !important',
      },
    },
  };
});
export default (props: Props) => {
  const { styles } = useStyles();

  if (props.type === 'grey') {
    return (
      <Alert
        {...omit(props, 'type')}
        style={{ backgroundColor: '#f1f1f2', border: '1px solid #f1f1f2', ...props.style }}
        className={styles.greyIcon}
      />
    );
  }
  return <Alert type={props.type} {...omit(props, 'type')} />;
};
