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
 * 微信扫码登录
 *
 * @constructor
 */
const WeChatScanCode = (props: { isCreate: boolean }) => {
  const { isCreate } = props;
  const intl = useIntl();

  return (
    <>
      <ProFormText
        name={['config', 'appId']}
        label="AppId"
        rules={[{ required: true }]}
        extra={intl.formatMessage({
          id: 'pages.authn.identity_provider.config.wechat_scan_code.app_id.extra',
        })}
        placeholder={intl.formatMessage({
          id: 'pages.authn.identity_provider.config.wechat_scan_code.app_id.placeholder',
        })}
        fieldProps={{ autoComplete: 'off' }}
      />
      <ProFormText.Password
        name={['config', 'appSecret']}
        label="AppSecret"
        rules={[{ required: true }]}
        extra={intl.formatMessage({
          id: 'pages.authn.identity_provider.config.wechat_scan_code.app_secret.extra',
        })}
        placeholder={intl.formatMessage({
          id: 'pages.authn.identity_provider.config.wechat_scan_code.app_secret.placeholder',
        })}
        fieldProps={{ autoComplete: 'new-password' }}
      />
      {!isCreate && <CallbackUrl />}
    </>
  );
};
export default WeChatScanCode;
