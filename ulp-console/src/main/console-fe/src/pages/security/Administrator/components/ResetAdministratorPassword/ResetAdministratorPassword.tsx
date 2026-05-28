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
import { ModalForm, ProFormText } from '@ant-design/pro-components';
import { Spin } from 'antd';
import { Base64 } from 'js-base64';
import * as React from 'react';
import { useEffect, useRef, useState } from 'react';
import { useIntl } from '@umijs/max';

export default (props: {
  id: string;
  visible: boolean;
  onCancel?: (e: React.MouseEvent<HTMLElement>) => void;
  onFinish: (formData: Record<string, string>) => Promise<boolean | void>;
}) => {
  const { visible, onCancel, onFinish, id } = props;
  const [loading, setLoading] = useState<boolean>(false);
  const formRef = useRef<ProFormInstance>();
  const intl = useIntl();

  useEffect(() => {
    setLoading(true);
    formRef.current?.setFieldsValue({ id });
    setLoading(false);
  }, [visible, id]);

  return (
    <ModalForm
      title={intl.formatMessage({ id: 'pages.setting.administrator.reset_password_modal' })}
      width={'460px'}
      formRef={formRef}
      labelAlign={'right'}
      preserve={false}
      layout={'vertical'}
      autoFocusFirstInput
      open={visible}
      modalProps={{
        destroyOnClose: true,
        onCancel: onCancel,
      }}
      onFinish={async (formData: { password: string }) => {
        setLoading(true);
        const password = Base64.encode(formData.password, true);
        onFinish({ id, password }).finally(() => {
          setLoading(false);
        });
      }}
    >
      <Spin spinning={loading}>
        <ProFormText.Password
          name="password"
          label={intl.formatMessage({
            id: 'pages.setting.administrator.reset_password_modal.from.password',
          })}
          placeholder={intl.formatMessage({
            id: 'pages.setting.administrator.reset_password_modal.from.password.placeholder',
          })}
          fieldProps={{ autoComplete: 'off' }}
          rules={[
            {
              required: true,
              message: intl.formatMessage({
                id: 'pages.setting.administrator.reset_password_modal.from.password.rule.0.message',
              }),
            },
          ]}
        />
        <ProFormText.Password
          label={intl.formatMessage({
            id: 'pages.setting.administrator.reset_password_modal.from.confirm_password',
          })}
          placeholder={intl.formatMessage({
            id: 'pages.setting.administrator.reset_password_modal.from.confirm_password.placeholder',
          })}
          name={'confirmPassword'}
          fieldProps={{ autoComplete: 'off' }}
          rules={[
            {
              required: true,
              message: intl.formatMessage({
                id: 'pages.setting.administrator.reset_password_modal.from.confirm_password.rule.0.message',
              }),
            },
            ({ getFieldValue }) => ({
              validator(_, value) {
                if (!value || getFieldValue('password') === value) {
                  return Promise.resolve();
                }
                return Promise.reject(
                  new Error(intl.formatMessage({ id: 'app.password.not_match' })),
                );
              },
            }),
          ]}
        />
      </Spin>
    </ModalForm>
  );
};
