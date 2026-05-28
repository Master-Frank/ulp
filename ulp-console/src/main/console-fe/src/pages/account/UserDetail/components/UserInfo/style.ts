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

const useStyle = createStyles(({ prefixCls, token }, props) => {
  const antCls = `.${prefixCls}`;
  const prefix = `${props}`;
  return {
    main: {
      [`.${prefix}-descriptions`]: {
        [`${antCls}-descriptions-small ${antCls}-descriptions-row > th, ${antCls}-descriptions-small ${antCls}-descriptions-row > td`]:
          {
            paddingBottom: '16px',
          },
      },
      [`.${prefix}-content`]: {
        width: '100%',
        display: 'flex',
        flexDirection: 'row',
        justifyContent: 'space-between',
        alignItems: 'center',
        [`&-status`]: {
          width: '2%',
          height: '80px',
          borderRadius: '8px 0 0 8px',
          background: token.colorSuccess,
          marginRight: '10px',
        },
        [`&-title`]: {
          width: '63%',
          display: 'flex',
          flexDirection: 'row',
          alignItems: 'center',
          [`&-avatar`]: {
            display: 'inline-flex',
            alignItems: 'center',
          },
          [`&-text`]: {
            display: 'inline-block',
            paddingLeft: '10px',
            textAlign: 'center',
            whiteSpace: 'nowrap',
            textOverflow: 'ellipsis',
            overflow: 'hidden',
            fontWeight: '600',
          },
        },
        [`&-operate`]: {
          width: '35%',
          textAlign: 'center',
          marginRight: '10px',
        },
      },
    },
  };
});

export default useStyle;
