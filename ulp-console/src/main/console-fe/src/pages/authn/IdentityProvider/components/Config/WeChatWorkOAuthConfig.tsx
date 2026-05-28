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
import { ProFormText } from '@ant-design/pro-components';
import { Input } from 'antd';
import CallbackUrl from './CallbackUrl';
import { useIntl } from '@umijs/max';

/**
 * 企业微信认证
 *
 * @constructor
 */
const WeWorkScanCode = (props: { isCreate: boolean }) => {
  const { isCreate } = props;
  const intl = useIntl();

  return (
    <>
      <ProFormText
        name={['config', 'corpId']}
        label={intl.formatMessage({
          id: 'pages.authn.identity_provider.config.wework_scan_code.corp_id',
        })}
        rules={[{ required: true }]}
        extra={intl.formatMessage({
          id: 'pages.authn.identity_provider.config.wework_scan_code.corp_id.extra',
        })}
      >
        <Input
          autoComplete="off"
          placeholder={intl.formatMessage({
            id: 'pages.authn.identity_provider.config.wework_scan_code.corp_id.placeholder',
          })}
        />
      </ProFormText>
      <ProFormText
        name={['config', 'agentId']}
        label="AgentId"
        rules={[{ required: true }]}
        extra={intl.formatMessage({
          id: 'pages.authn.identity_provider.config.wework_scan_code.agent_id.extra',
        })}
      >
        <Input
          autoComplete="off"
          placeholder={intl.formatMessage({
            id: 'pages.authn.identity_provider.config.wework_scan_code.agent_id.placeholder',
          })}
        />
      </ProFormText>
      <ProFormText.Password
        name={['config', 'appSecret']}
        label="Secret"
        rules={[{ required: true }]}
        extra={intl.formatMessage({
          id: 'pages.authn.identity_provider.config.wework_scan_code.app_secret.extra',
        })}
        fieldProps={{ autoComplete: 'new-password' }}
        placeholder={intl.formatMessage({
          id: 'pages.authn.identity_provider.config.wework_scan_code.app_secret.placeholder',
        })}
      />
      {!isCreate && <CallbackUrl />}
    </>
  );
};

export default WeWorkScanCode;
