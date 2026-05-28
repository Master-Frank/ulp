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

const useStyle = createStyles(({ token, prefixCls }, props) => {
  const antCls = `.${prefixCls}`;
  const prefix = `${props}`;
  return {
    main: {
      [`.${prefix}`]: {
        [`&-title`]: {
          marginTop: 0,
          marginBottom: '0',
          fontSize: '16px',
        },
        [`${antCls}-avatar > img`]: {
          objectFit: 'fill',
        },
        [`${antCls}-card-head`]: {
          borderBottom: 'none',
        },
        [`${antCls}-card-head-title`]: {
          padding: '24px 0',
          lineHeight: '32px',
        },
        [`${antCls}-card-extra`]: {
          padding: '24px 0',
        },
        [`${antCls}-list-pagination`]: {
          marginTop: '24px',
        },
        [`${antCls}-avatar-lg`]: {
          width: '48px',
          height: '48px',
          lineHeight: '48px',
        },
        [`${antCls}-list-item-action`]: {
          marginInlineStart: '15px',
        },
      },
      [`@media  (max-width: ${token.screenXS}px)`]: {
        [`.${prefixCls}-list`]: {},
      },
      [`@media  (max-width: ${token.screenSM}px)`]: {
        [`.${prefixCls}-list`]: {},
      },
      [`@media  (max-width: ${token.screenMD}px)`]: {
        [`.${prefixCls}-list`]: {},
      },
      [`@media  (max-width: ${token.screenLG}px) and @media (min-width: ${token.screenMD}px)`]: {
        [`.${prefixCls}-list`]: {},
      },
      [`@media  (max-width: ${token.screenXL}px)`]: {
        [`.${prefixCls}-list`]: {},
      },
      [`@media  (max-width: 1400px)`]: {
        [`.${prefixCls}-list`]: {},
      },
    },
  };
});
export default useStyle;
