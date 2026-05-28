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
import CallbackUrl from './CallbackUrl';
import { useIntl } from '@umijs/max';

/**
 * AliPay Oauth 登录
 *
 * @constructor
 */
const QqOauthConfig = (props: { isCreate: boolean }) => {
  const { isCreate } = props;
  const intl = useIntl();

  return (
    <>
      <ProFormText
        name={['config', 'appId']}
        label={intl.formatMessage({
          id: 'pages.authn.identity_provider.config.alipay_oauth.app_id',
        })}
        rules={[{ required: true }]}
        fieldProps={{ autoComplete: 'off' }}
        placeholder={intl.formatMessage({
          id: 'pages.authn.identity_provider.config.alipay_oauth.app_id.placeholder',
        })}
      />
      <ProFormText.Password
        rules={[{ required: true }]}
        name={['config', 'appPrivateKey']}
        label={intl.formatMessage({
          id: 'pages.authn.identity_provider.config.alipay_oauth.app_private_key',
        })}
        placeholder={intl.formatMessage({
          id: 'pages.authn.identity_provider.config.alipay_oauth.app_private_key.placeholder',
        })}
        fieldProps={{ autoComplete: 'off' }}
      />
      <ProFormText.Password
        rules={[{ required: true }]}
        name={['config', 'alipayPublicKey']}
        label={intl.formatMessage({
          id: 'pages.authn.identity_provider.config.alipay_oauth.alipay_public_key',
        })}
        placeholder={intl.formatMessage({
          id: 'pages.authn.identity_provider.config.alipay_oauth.alipay_public_key.placeholder',
        })}
        fieldProps={{ autoComplete: 'off' }}
      />
      {!isCreate && <CallbackUrl />}
    </>
  );
};
export default QqOauthConfig;
