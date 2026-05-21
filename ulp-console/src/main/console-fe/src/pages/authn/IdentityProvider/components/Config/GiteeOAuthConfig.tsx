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
 * Gitee Oauth 登录
 *
 * @constructor
 */
const GiteeOAuthConfig = (props: { isCreate: boolean }) => {
  const { isCreate } = props;
  const intl = useIntl();

  return (
    <>
      <ProFormText
        name={['config', 'clientId']}
        label={intl.formatMessage({
          id: 'pages.authn.identity_provider.config.gitee_oauth.client_id',
        })}
        rules={[{ required: true }]}
        fieldProps={{ autoComplete: 'off' }}
        placeholder={intl.formatMessage({
          id: 'pages.authn.identity_provider.config.gitee_oauth.client_id.placeholder',
        })}
      />
      <ProFormText.Password
        rules={[{ required: true }]}
        name={['config', 'clientSecret']}
        label={intl.formatMessage({
          id: 'pages.authn.identity_provider.config.gitee_oauth.client_secret',
        })}
        placeholder={intl.formatMessage({
          id: 'pages.authn.identity_provider.config.gitee_oauth.client_secret.placeholder',
        })}
        fieldProps={{ autoComplete: 'off' }}
      />
      {!isCreate && <CallbackUrl />}
    </>
  );
};
export default GiteeOAuthConfig;
