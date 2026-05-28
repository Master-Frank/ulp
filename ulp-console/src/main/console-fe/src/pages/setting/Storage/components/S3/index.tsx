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
        name={['config', 'endpoint']}
        label={intl.formatMessage({
          id: `pages.setting.storage_provider.provider.s3.endpoint`,
        })}
        placeholder={intl.formatMessage({
          id: `pages.setting.storage_provider.provider.s3.endpoint.placeholder`,
        })}
        rules={[
          {
            required: true,
            message: intl.formatMessage({
              id: `pages.setting.storage_provider.provider.s3.endpoint.rule.0.message`,
            }),
          },
        ]}
      />
      <ProFormText
        name={['config', 'domain']}
        label={intl.formatMessage({
          id: `pages.setting.storage_provider.provider.s3.domain`,
        })}
        placeholder={intl.formatMessage({
          id: `pages.setting.storage_provider.provider.s3.domain.placeholder`,
        })}
        rules={[
          {
            required: true,
            message: intl.formatMessage({
              id: `pages.setting.storage_provider.provider.s3.domain.rule.0.message`,
            }),
          },
        ]}
        fieldProps={{ autoComplete: 'off' }}
      />
      <ProFormText
        name={['config', 'accessKeyId']}
        label="AccessKeyId"
        placeholder={intl.formatMessage({
          id: `pages.setting.storage_provider.provider.s3.access_key_id.placeholder`,
        })}
        rules={[
          {
            required: true,
            message: intl.formatMessage({
              id: `pages.setting.storage_provider.provider.s3.access_key_id.rule.0.message`,
            }),
          },
        ]}
        fieldProps={{
          autoComplete: 'new-password',
        }}
      />
      <ProFormText.Password
        name={['config', 'secretAccessKey']}
        label="SecretAccessKey"
        placeholder={intl.formatMessage({
          id: `pages.setting.storage_provider.provider.s3.secret_access_key.placeholder`,
        })}
        rules={[
          {
            required: true,
            message: intl.formatMessage({
              id: `pages.setting.storage_provider.provider.s3.secret_access_key.rule.0.message`,
            }),
          },
        ]}
        fieldProps={{ autoComplete: 'off' }}
      />
      <ProFormText
        name={['config', 'bucket']}
        label={'Bucket'}
        placeholder={intl.formatMessage({
          id: `pages.setting.storage_provider.provider.s3.bucket.placeholder`,
        })}
        rules={[
          {
            required: true,
            message: intl.formatMessage({
              id: `pages.setting.storage_provider.provider.s3.bucket.rule.0.message`,
            }),
          },
        ]}
        fieldProps={{ autoComplete: 'off' }}
      />
      <ProFormText
        name={['config', 'region']}
        label={'Region'}
        placeholder={intl.formatMessage({
          id: `pages.setting.storage_provider.provider.s3.region.placeholder`,
        })}
        fieldProps={{ autoComplete: 'off' }}
      />
    </>
  );
};
