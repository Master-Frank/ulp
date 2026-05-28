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
import { EyeInvisibleOutlined, EyeTwoTone } from '@ant-design/icons';
import { ProFormSelect, ProFormText } from '@ant-design/pro-components';
import { Region } from '@/pages/setting/Storage/constant';
import { useIntl } from '@umijs/max';

/**
 * 腾讯云
 */
const Tencent = () => {
  const intl = useIntl();
  return (
    <>
      <ProFormSelect
        name="region"
        label={intl.formatMessage({
          id: 'pages.setting.message.sms_provider.provider.tencent.region',
        })}
        placeholder={intl.formatMessage({
          id: 'pages.setting.message.sms_provider.provider.tencent.region.placeholder',
        })}
        initialValue={Region.GUANGZHOU}
        rules={[
          {
            required: true,
            message: intl.formatMessage({
              id: 'pages.setting.message.sms_provider.provider.tencent.region.rule.0.message',
            }),
          },
        ]}
        options={[
          {
            label: intl.formatMessage({
              id: 'pages.setting.message.sms_provider.provider.tencent.region.beijing',
            }),
            value: Region.BEIJING,
          },
          {
            label: intl.formatMessage({
              id: 'pages.setting.message.sms_provider.provider.tencent.region.guangzhou',
            }),
            value: Region.GUANGZHOU,
          },
          {
            label: intl.formatMessage({
              id: 'pages.setting.message.sms_provider.provider.tencent.region.nanjing',
            }),
            value: Region.NANJING,
          },
        ]}
      />
      <ProFormText
        name="secretId"
        label="SecretId"
        placeholder={intl.formatMessage({
          id: 'pages.setting.message.sms_provider.provider.tencent.secret_id.placeholder',
        })}
        rules={[
          {
            required: true,
            message: intl.formatMessage({
              id: 'pages.setting.message.sms_provider.provider.tencent.secret_id.rule.0.message',
            }),
          },
        ]}
        fieldProps={{ autoComplete: 'off' }}
      />
      <ProFormText.Password
        name="secretKey"
        label="SecretKey"
        rules={[
          {
            required: true,
            message: intl.formatMessage({
              id: 'pages.setting.message.sms_provider.provider.tencent.secret_key.rule.0.message',
            }),
          },
        ]}
        placeholder={intl.formatMessage({
          id: 'pages.setting.message.sms_provider.provider.tencent.secret_key.placeholder',
        })}
        fieldProps={{
          iconRender: (value) => {
            return value ? <EyeTwoTone /> : <EyeInvisibleOutlined />;
          },
        }}
      />
      <ProFormText
        name="sdkAppId"
        label={intl.formatMessage({
          id: 'pages.setting.message.sms_provider.provider.tencent.sdk_app_id',
        })}
        placeholder={intl.formatMessage({
          id: 'pages.setting.message.sms_provider.provider.tencent.sdk_app_id.placeholder',
        })}
        rules={[
          {
            required: true,
            message: intl.formatMessage({
              id: 'pages.setting.message.sms_provider.provider.tencent.sdk_app_id.rule.0.message',
            }),
          },
        ]}
        fieldProps={{ autoComplete: 'off' }}
      />
      <ProFormText
        name="signName"
        label={intl.formatMessage({
          id: 'pages.setting.message.sms_provider.provider.tencent.sign_name',
        })}
        placeholder={intl.formatMessage({
          id: 'pages.setting.message.sms_provider.provider.tencent.sign_name.placeholder',
        })}
        rules={[
          {
            required: true,
            message: intl.formatMessage({
              id: 'pages.setting.message.sms_provider.provider.tencent.sign_name.rule.0.message',
            }),
          },
        ]}
        fieldProps={{ autoComplete: 'off' }}
      />
    </>
  );
};
export default Tencent;
