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
import type { ProFormInstance } from '@ant-design/pro-components';
import { ProFormText } from '@ant-design/pro-components';
import { Typography } from 'antd';
import type { RefObject } from 'react';
import { BASIC_CONFIG_FROM_PARAM } from '../../constant';
import DingTalkConfig from './DingTalkConfig';
import FeiShuConfig from './FeiShuConfig';
import { IdentitySourceProvider } from '@/constant';
import { useIntl } from '@@/exports';

const { Paragraph } = Typography;
export interface BasicConfigInstance {
  /**验证连接*/
  configValidator: () => Promise<boolean>;
}

type BasicConfigProps = {
  provider: IdentitySourceProvider;
  configured: boolean;
  formRef?: RefObject<ProFormInstance | undefined>;
  basicConfigRef: RefObject<BasicConfigInstance | undefined>;
  onConfigValidator: (config: Record<string, string>) => Promise<boolean>;
};

export default (props: BasicConfigProps) => {
  const intl = useIntl();
  const { provider, configured, basicConfigRef, onConfigValidator, formRef } = props;
  return (
    <>
      {/*钉钉*/}
      {provider === IdentitySourceProvider.dingtalk && (
        <DingTalkConfig
          configured={configured}
          basicConfigRef={basicConfigRef}
          onConfigValidator={onConfigValidator}
          formRef={formRef}
        />
      )}
      {/*飞书*/}
      {provider === IdentitySourceProvider.feishu && (
        <FeiShuConfig
          configured={configured}
          basicConfigRef={basicConfigRef}
          onConfigValidator={onConfigValidator}
          formRef={formRef}
        />
      )}
      <ProFormText
        label={intl.formatMessage({
          id: 'pages.account.identity_source_detail.common.callback_uri',
        })}
        name={BASIC_CONFIG_FROM_PARAM.callbackUrl}
        proFieldProps={{
          render: (value: string) => {
            return (
              value && (
                <Paragraph copyable={{ text: value }} style={{ marginBottom: '0' }}>
                  <span
                    dangerouslySetInnerHTML={{
                      __html: `<span>${value}</span>`,
                    }}
                  />
                </Paragraph>
              )
            );
          },
        }}
        readonly
        fieldProps={{ autoComplete: 'off' }}
      />
    </>
  );
};
