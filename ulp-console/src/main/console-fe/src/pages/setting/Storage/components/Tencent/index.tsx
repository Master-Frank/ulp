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
import { useIntl } from '@umijs/max';

export default () => {
  const intl = useIntl();
  return (
    <>
      <ProFormText
        name={['config', 'domain']}
        label={intl.formatMessage({
          id: 'pages.setting.storage_provider.provider.tencent_cos.domain',
        })}
        placeholder={intl.formatMessage({
          id: 'pages.setting.storage_provider.provider.tencent_cos.domain.placeholder',
        })}
        rules={[
          {
            required: true,
            message: intl.formatMessage({
              id: 'pages.setting.storage_provider.provider.tencent_cos.domain.rule.0.message',
            }),
          },
        ]}
        fieldProps={{ autoComplete: 'off' }}
      />
      <ProFormText
        name={['config', 'appId']}
        label="AppId"
        placeholder={intl.formatMessage({
          id: 'pages.setting.storage_provider.provider.tencent_cos.app_id.placeholder',
        })}
        rules={[
          {
            required: true,
            message: intl.formatMessage({
              id: 'pages.setting.storage_provider.provider.tencent_cos.app_id.rule.0.message',
            }),
          },
        ]}
        fieldProps={{
          autoComplete: 'off',
        }}
      />
      <ProFormText
        name={['config', 'secretId']}
        label="SecretId"
        placeholder={intl.formatMessage({
          id: 'pages.setting.storage_provider.provider.tencent_cos.secret_id.placeholder',
        })}
        rules={[
          {
            required: true,
            message: intl.formatMessage({
              id: 'pages.setting.storage_provider.provider.tencent_cos.secret_id.rule.0.message',
            }),
          },
        ]}
        fieldProps={{
          autoComplete: 'new-password',
        }}
      />
      <ProFormText.Password
        name={['config', 'secretKey']}
        label="SecretKey"
        placeholder={intl.formatMessage({
          id: 'pages.setting.storage_provider.provider.tencent_cos.secret_key.placeholder',
        })}
        rules={[
          {
            required: true,
            message: intl.formatMessage({
              id: 'pages.setting.storage_provider.provider.tencent_cos.secret_key.rule.0.message',
            }),
          },
        ]}
        fieldProps={{ autoComplete: 'off' }}
      />
      <ProFormText
        name={['config', 'region']}
        label={'Region'}
        placeholder={intl.formatMessage({
          id: 'pages.setting.storage_provider.provider.tencent_cos.region.placeholder',
        })}
        rules={[
          {
            required: true,
            message: intl.formatMessage({
              id: 'pages.setting.storage_provider.provider.tencent_cos.region.rule.0.message',
            }),
          },
        ]}
        fieldProps={{ autoComplete: 'off' }}
      />
      <ProFormText
        name={['config', 'bucket']}
        label={'Bucket'}
        placeholder={intl.formatMessage({
          id: 'pages.setting.storage_provider.provider.tencent_cos.bucket.placeholder',
        })}
        rules={[
          {
            required: true,
            message: intl.formatMessage({
              id: 'pages.setting.storage_provider.provider.tencent_cos.bucket.rule.0.message',
            }),
          },
        ]}
        fieldProps={{ autoComplete: 'off' }}
      />
    </>
  );
};
