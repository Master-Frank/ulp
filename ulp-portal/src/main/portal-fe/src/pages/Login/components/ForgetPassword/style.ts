/*
 * ulp-portal - United Login Platform
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

const useStyle = createStyles((_, props) => {
  const prefixCls = `${props}`;
  return {
    main: {
      width: '328px',
      [`.${prefixCls}`]: {
        ['&-success-box']: {
          padding: '40px',
          textAlign: 'center',
        },
        ['&-success']: {
          width: '100%',
          textAlign: 'center',
          marginTop: '32px',
          ['&-button']: {
            width: '100%',
          },
          ['&-title']: {
            fontWeight: '600',
            fontSize: '24px',
            marginBottom: '24px',
          },
          ['&-desc']: {
            fontWeight: '400',
            fontSize: '14px',
            marginBottom: '32px',
            lineHeight: '20px',
            color: '#545968',
          },
        },
        ['&-back']: {
          width: '100%',
          marginBottom: '24px',
        },
        ['&-back-time']: {
          display: 'flex',
          justifyContent: 'center',
          fontSize: '14px',
          color: '#215ae5',
          cursor: 'pointer',
          marginTop: '16px',
          height: '20px',
        },
      },
    },
  };
});

export default useStyle;
