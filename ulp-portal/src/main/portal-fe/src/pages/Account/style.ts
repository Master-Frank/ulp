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
import type { GenerateStyle, ProAliasToken } from '@ant-design/pro-components';
import { useStyle as useAntdStyle } from '@ant-design/pro-components';
import { ConfigProvider } from 'antd';
import { useContext } from 'react';

const { ConfigContext } = ConfigProvider;

interface AccountToken extends ProAliasToken {
  antCls: string;
  prefixCls: string;
}

const genActionsStyle: GenerateStyle<AccountToken> = (token) => {
  const { prefixCls, antCls } = token;

  return {
    [`${prefixCls}`]: {
      ['&-main']: {
        display: 'flex',
        width: '100%',
        height: '100%',
        paddingTop: '16px',
        paddingBottom: '16px',
        'background-color': `${token.colorBgBase}`,
      },
      ['&-left']: {
        width: '224px',
        [`${antCls}-menu-light${antCls}-menu-root${antCls}-menu-inline`]: {
          'border-inline-end': '1px solid rgba(5, 5, 5, 0.06)',
          height: '100%',
        },
        [`${antCls}-menu-light:not(${antCls}-menu-horizontal) ${antCls}-menu-item-selected`]: {
          'background-color': `${token.layout?.sider?.colorBgMenuItemSelected}`,
          color: `${token.layout?.sider?.colorTextMenuSelected}`,
        },
      },
      ['&-right']: {
        flex: 1,
        padding: '8px 40px',
        [`${antCls}-list ${antCls}-list-item`]: {
          'padding-inline-start': 0,
        },
        ['&-title']: {
          marginBottom: '12px',
          color: `${token.colorTextHeading}`,
          fontWeight: 500,
          fontSize: '20px',
          lineHeight: '28px',
        },
      },
    },
    [`@media screen and (max-width: ${token.screenMD}px)`]: {
      [`${prefixCls}`]: {
        ['&-main']: {
          flexDirection: 'column',
        },
        ['&-left']: {
          width: '100%',
          border: 'none',
        },
        ['&-right']: {
          padding: '40px',
        },
      },
    },
  };
};

export default function useStyle(prefixCls?: string) {
  const { getPrefixCls } = useContext(ConfigContext || ConfigProvider.ConfigContext);
  const antCls = `.${getPrefixCls()}`;

  return useAntdStyle('AccountToken', (token) => {
    const accountToken: AccountToken = {
      ...token,
      prefixCls: `.${prefixCls}`,
      antCls,
    };

    return [genActionsStyle(accountToken)];
  });
}
