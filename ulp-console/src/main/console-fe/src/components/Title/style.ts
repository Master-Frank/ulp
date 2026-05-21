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
import type { GenerateStyle, ProAliasToken } from '@ant-design/pro-components';
import { useStyle as useAntdStyle } from '@ant-design/pro-components';
import { ConfigProvider } from 'antd';
import { useContext } from 'react';

const { ConfigContext } = ConfigProvider;

interface TitleComponentToken extends ProAliasToken {
  antCls: string;
  prefixCls: string;
}

const genActionsStyle: GenerateStyle<TitleComponentToken> = (token) => {
  const { prefixCls } = token;

  return {
    [`${prefixCls}`]: {
      display: 'flex',
      alignItems: 'center',
      justifyContent: 'space-between',
      marginBottom: '24px',
      '&-title': {
        display: 'flex',
        alignItems: 'center',
        height: '36px',
        color: '#181818',
        fontSize: '30px',
        lineHeight: '36px',
        '&-back-btn': {
          marginInlineStart: '8px',
        },
      },
    },
  };
};

export default function useStyle(prefixCls?: string) {
  const { getPrefixCls } = useContext(ConfigContext || ConfigProvider.ConfigContext);
  const antCls = `.${getPrefixCls()}`;

  return useAntdStyle('TitleComponent', (token) => {
    const titleComponentToken: TitleComponentToken = {
      ...token,
      prefixCls: `.${prefixCls}`,
      antCls,
    };

    return [genActionsStyle(titleComponentToken)];
  });
}
