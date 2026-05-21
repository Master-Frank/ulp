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
import {
  ModalForm,
  ProFormDependency,
  ProFormSelect,
  ProFormText,
  ProFormTextArea,
} from '@ant-design/pro-components';
import { useAsyncEffect } from 'ahooks';
import { Avatar, Space, Spin } from 'antd';
import { useForm } from 'antd/es/form/Form';
import React, { useState } from 'react';
import {
  DRAWER_FORM_ITEM_LAYOUT,
  IdentityProviderCategory,
  IdentityProviderType,
} from '../../constant';
import Config from '../Config';
import { useIntl } from '@umijs/max';
import { ICON_LIST } from '@/components/IconFont/constant';

export type CreateDrawerProps = {
  visible?: boolean;
  category: IdentityProviderCategory;
  onCancel: () => void;
  onFinish: (values: Record<string, string>) => Promise<boolean | void>;
};
export default (props: CreateDrawerProps) => {
  const { visible, category, onCancel, onFinish } = props;
  const [form] = useForm();
  const [loading, setLoading] = useState(false);
  const intl = useIntl();
  useAsyncEffect(async () => {
    if (visible) {
      if (category === IdentityProviderCategory.enterprise) {
        form?.setFieldsValue({
          displayed: true,
          type: IdentityProviderType.dingtalk_oauth,
        });
      }
      if (category === IdentityProviderCategory.social) {
        form?.setFieldsValue({
          displayed: true,
          type: IdentityProviderType.wechat_qr,
        });
      }
    }
  }, [visible, category]);

  return (
    <ModalForm
      title={intl.formatMessage({
        id: 'pages.authn.identity_provider.create_modal_title',
      })}
      width={580}
      modalProps={{
        forceRender: true,
        onCancel: () => {
          onCancel();
          form?.resetFields();
        },
        destroyOnClose: true,
      }}
      open={visible}
      form={form}
      scrollToFirstError
      layout={'horizontal'}
      {...DRAWER_FORM_ITEM_LAYOUT}
      onFinish={async (values: Record<string, string>) => {
        setLoading(true);
        await onFinish(values).finally(() => {
          setLoading(false);
        });
      }}
    >
      <Spin spinning={loading}>
        {category === IdentityProviderCategory.social && (
          <>
            <ProFormText hidden name={'category'} initialValue={IdentityProviderCategory.social} />
            <ProFormSelect
              label={intl.formatMessage({
                id: 'pages.authn.identity_provider.create_modal.form.type',
              })}
              name="type"
              placeholder={intl.formatMessage({
                id: 'pages.authn.identity_provider.create_modal.form.type.placeholder',
              })}
              rules={[
                {
                  message: intl.formatMessage({
                    id: 'pages.authn.identity_provider.create_modal.form.type.rule.0.message',
                  }),
                  required: true,
                },
              ]}
              fieldProps={{
                optionRender: ({ data }) => {
                  return (
                    <Space>
                      <Avatar shape={'square'} src={ICON_LIST[data?.icon]} />
                      {data.label}
                    </Space>
                  );
                },
              }}
              options={[
                {
                  value: IdentityProviderType.wechat_qr,
                  label: intl.formatMessage({
                    id: 'pages.authn.identity_provider.create_modal.form.type.wechat_oauth',
                  }),
                  icon: IdentityProviderType.wechat_qr,
                },
                {
                  value: IdentityProviderType.qq_oauth,
                  label: intl.formatMessage({
                    id: 'pages.authn.identity_provider.create_modal.form.type.qq',
                  }),
                  icon: IdentityProviderType.qq_oauth,
                },
                {
                  value: IdentityProviderType.alipay_oauth,
                  label: intl.formatMessage({
                    id: 'pages.authn.identity_provider.create_modal.form.type.alipay_oauth',
                  }),
                  icon: IdentityProviderType.alipay_oauth,
                },
                {
                  value: IdentityProviderType.github_oauth,
                  label: intl.formatMessage({
                    id: 'pages.authn.identity_provider.create_modal.form.type.github',
                  }),
                  icon: IdentityProviderType.github_oauth,
                },
                {
                  value: IdentityProviderType.gitee_oauth,
                  label: intl.formatMessage({
                    id: 'pages.authn.identity_provider.create_modal.form.type.gitee',
                  }),
                  icon: IdentityProviderType.gitee_oauth,
                },
              ]}
            />
          </>
        )}
        {category === IdentityProviderCategory.enterprise && (
          <>
            <ProFormText
              hidden
              name={'category'}
              initialValue={IdentityProviderCategory.enterprise}
            />
            <ProFormSelect
              label={intl.formatMessage({
                id: 'pages.authn.identity_provider.create_modal.form.type',
              })}
              name="type"
              placeholder={intl.formatMessage({
                id: 'pages.authn.identity_provider.create_modal.form.type.placeholder',
              })}
              rules={[
                {
                  message: intl.formatMessage({
                    id: 'pages.authn.identity_provider.create_modal.form.type.rule.0.message',
                  }),
                  required: true,
                },
              ]}
              fieldProps={{
                optionRender: ({ data }) => {
                  return (
                    <Space>
                      <Avatar shape={'square'} src={ICON_LIST[data?.icon]} />
                      {data.label}
                    </Space>
                  );
                },
              }}
              options={[
                {
                  value: IdentityProviderType.dingtalk_oauth,
                  label: intl.formatMessage({
                    id: 'pages.authn.identity_provider.create_modal.form.type.dingtalk_oauth',
                  }),
                  icon: IdentityProviderType.dingtalk_oauth,
                },
                {
                  value: IdentityProviderType.feishu_oauth,
                  label: intl.formatMessage({
                    id: 'pages.authn.identity_provider.create_modal.form.type.feishu_oauth',
                  }),
                  icon: IdentityProviderType.feishu_oauth,
                },
                {
                  value: IdentityProviderType.wechatwork_oauth,
                  label: intl.formatMessage({
                    id: 'pages.authn.identity_provider.create_modal.form.type.wechatwork_oauth',
                  }),
                  icon: IdentityProviderType.wechatwork_oauth,
                },
              ]}
            />
          </>
        )}
        <ProFormText
          label={intl.formatMessage({
            id: 'pages.authn.identity_provider.create_modal.form.name',
          })}
          name="name"
          placeholder={intl.formatMessage({
            id: 'pages.authn.identity_provider.create_modal.form.name.placeholder',
          })}
          rules={[
            {
              message: intl.formatMessage({
                id: 'pages.authn.identity_provider.create_modal.form.name.rule.0.message',
              }),
              required: true,
            },
          ]}
          fieldProps={{
            autoComplete: 'off',
          }}
        />
        <ProFormDependency name={['type']}>
          {({ type }) => {
            return <Config type={type} isCreate />;
          }}
        </ProFormDependency>
        <ProFormTextArea
          name="remark"
          fieldProps={{ rows: 2, maxLength: 20, showCount: false }}
          placeholder={intl.formatMessage({
            id: 'pages.authn.identity_provider.create_modal.form.remark.placeholder',
          })}
          label={intl.formatMessage({
            id: 'pages.authn.identity_provider.create_modal.form.remark',
          })}
        />
      </Spin>
    </ModalForm>
  );
};
