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
  const prefix = `.${props}`;
  return {
    main: {
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
      [`${antCls}-pro-list ${antCls}-pro-list-row`]: {
        paddingLeft: '10px',
        paddingRight: '10px',
      },
      [`${antCls}-list-item`]: {
        paddingLeft: '10px',
        paddingRight: '10px',
      },
      [`${antCls}-pro-list-row-content`]: {
        flex: 0,
        margin: '0',
      },
      [`${prefix}`]: {
        [`&-content`]: {
          marginInlineStart: '15px',
          '& > div': {
            marginInlineStart: '0',
          },
        },
        [`&-item-content`]: {
          display: 'block',
          flex: 'none',
          width: '100%',
          [`${antCls}-pro-list-row-content`]: {
            flex: 0,
            margin: '0',
          },
        },
      },
      [`@media  (max-width: ${token.screenXS}px)`]: {
        [`${prefix}`]: {
          [`&-content`]: {
            marginInlineStart: '0',
            '& > div': {
              marginInlineStart: '0',
            },
          },
          [`&-item-content`]: {
            display: 'block',
            flex: 'none',
            width: '100%',
          },
        },
      },
      [`@media  (max-width: ${token.screenSM}px)`]: {
        [`${prefix}`]: {},
      },
      [`@media  (max-width: ${token.screenMD}px)`]: {
        [`${prefix}`]: {
          [`&-content`]: {
            '& > div': {
              display: 'block',
              '&:last-child': {
                top: '0',
                width: '100%',
              },
            },
          },
        },
      },
      [`@media (max-width: ${token.screenLG}px) and @media (min-width: ${token.screenMD}px)`]: {
        [`${prefix}`]: {
          [`&-content`]: {
            '& > div': {
              display: 'block',
              '&:last-child': {
                top: '0',
                width: '100%',
              },
            },
          },
        },
      },
      [`@media  (max-width: ${token.screenXL}px)`]: {
        [`${prefix}`]: {
          [`&-content`]: {
            '& > div': {
              marginInlineStart: '24px',
              '&:last-child': {
                top: '0',
              },
            },
          },
        },
      },
      [`@media  (max-width: 1400px)`]: {
        [`${prefix}`]: {
          [`&-content`]: {
            '& > div': {
              '&:last-child': {
                top: '0',
              },
            },
          },
        },
      },
    },
  };
});

export default useStyle;
