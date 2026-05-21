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
import { Container } from '@/components/Container';
import { GEO_IP_PROVIDER } from '@/constant';
import { getGeoIpConfig, saveGeoIpConfig } from './service';

import {
  PageContainer,
  ProCard,
  ProForm,
  ProFormDependency,
  ProFormSegmented,
} from '@ant-design/pro-components';
import { useAsyncEffect } from 'ahooks';
import { App, Form, Space, Spin } from 'antd';
import React, { useState } from 'react';
import MaxMind from './components/MaxMind';
import { useIntl } from '@umijs/max';

const layout = {
  labelCol: {
    xs: { span: 24 },
    sm: { span: 7 },
    md: { span: 6 },
  },
  wrapperCol: {
    xs: { span: 24 },
    sm: { span: 13 },
    md: { span: 14 },
  },
};

const tailFormItemLayout = {
  wrapperCol: {
    xs: {
      span: 24,
      offset: 0,
    },
    sm: {
      span: 17,
      offset: 7,
    },
    md: {
      span: 18,
      offset: 6,
    },
  },
};

const GeoIP = () => {
  const [form] = Form.useForm();
  const intl = useIntl();
  const { message } = App.useApp();
  const [loading, setLoading] = useState<boolean>(false);

  useAsyncEffect(async () => {
    form.resetFields();
    setLoading(true);
    const { success, result } = await getGeoIpConfig();
    if (success && result && result.enabled) {
      form.setFieldsValue({
        ...result,
      });
    }
    setLoading(false);
  }, []);

  return (
    <PageContainer content={intl.formatMessage({ id: 'pages.setting.geoip.desc' })}>
      <Spin spinning={loading}>
        <ProCard
          title={intl.formatMessage({
            id: 'pages.setting.geoip',
          })}
          headerBordered
          bordered={false}
          style={{ marginBottom: '24px' }}
        >
          <Container>
            <ProForm
              form={form}
              scrollToFirstError
              layout={'horizontal'}
              labelAlign={'right'}
              {...layout}
              initialValues={{ provider: GEO_IP_PROVIDER.DEFAULT }}
              onReset={() => {
                form.resetFields();
              }}
              submitter={{
                render: (_p, dom) => {
                  return (
                    <Form.Item {...tailFormItemLayout}>
                      <Space>{dom}</Space>
                    </Form.Item>
                  );
                },
              }}
              onFinish={async (values) => {
                setLoading(true);
                try {
                  const { success } = await saveGeoIpConfig({
                    provider: values.provider,
                    config: values.config,
                  });
                  if (success) {
                    message.success(intl.formatMessage({ id: 'app.save_success' }));
                    return Promise.resolve(true);
                  }
                } catch (e) {
                  return Promise.reject();
                } finally {
                  setLoading(false);
                }
              }}
            >
              <>
                <ProFormSegmented
                  name="provider"
                  label={intl.formatMessage({
                    id: 'pages.setting.geoip.form_select',
                  })}
                  rules={[{ required: true }]}
                  fieldProps={{
                    onChange: async (value) => {
                      setLoading(true);
                      form.resetFields();
                      form.setFieldsValue({
                        provider: value,
                      });
                      const { success, result } = await getGeoIpConfig();
                      if (success && result && result.enabled && value === result.provider) {
                        form.setFieldsValue({
                          ...result,
                        });
                      }
                      setLoading(false);
                    },
                  }}
                  request={async () => {
                    return [
                      {
                        value: GEO_IP_PROVIDER.DEFAULT,
                        label: intl.formatMessage({
                          id: 'pages.setting.geoip.form_select.option.default',
                        }),
                      },
                      {
                        value: GEO_IP_PROVIDER.MAXMIND,
                        label: intl.formatMessage({
                          id: 'pages.setting.geoip.form_select.option.maxmind',
                        }),
                      },
                    ];
                  }}
                />
                <ProFormDependency name={['provider']}>
                  {({ provider }) => {
                    return provider === GEO_IP_PROVIDER.MAXMIND && <MaxMind />;
                  }}
                </ProFormDependency>
              </>
            </ProForm>
          </Container>
        </ProCard>
      </Spin>
    </PageContainer>
  );
};

export default GeoIP;
