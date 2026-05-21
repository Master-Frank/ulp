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
import { createStyles } from 'antd-style';

const useStyle = createStyles(({ token, prefixCls }) => {
  const antCls = `.${prefixCls}`;
  return {
    main: {
      [`.user-group-remark`]: {
        boxSizing: 'border-box',
        width: '100%',
        margin: '0 !important',
        color: '#00000073',
        fontSize: '14px',
      },
      [`.user-group-detail`]: {
        color: `${token.colorLink} !important`,
      },
      [`${antCls}-avatar`]: {
        width: '32px !important',
        height: '32px !important',
        marginRight: '10px',
      },
      [`${antCls}-avatar-circle`]: {
        verticalAlign: 'middle',
        backgroundColor: token.colorPrimary,
      },
      [`${antCls}-card`]: {
        '&-meta-avatar': {
          paddingRight: '5px !important',
        },
      },
      [`${antCls}-card-meta-description`]: {
        marginTop: '15px',
        marginBottom: '15px',
      },
    },
  };
});

export default useStyle;
