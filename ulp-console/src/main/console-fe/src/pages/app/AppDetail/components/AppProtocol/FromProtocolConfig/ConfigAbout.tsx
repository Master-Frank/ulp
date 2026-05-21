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
import { ProForm, ProFormText } from '@ant-design/pro-components';
import { useAsyncEffect } from 'ahooks';
import { Form, Typography } from 'antd';
import { useIntl } from '@umijs/max';
import { createStyles } from 'antd-style';
import { ColProps } from 'antd/es/grid/col';

const useStyles = createStyles(({}) => ({
  config: {
    backgroundColor: '#f1f1f2',
  },
}));

/**
 * 配置信息
 *
 * @param props
 */
export default (props: {
  appId: string;
  protocolEndpoint: Record<string, string>;
  collapsed?: boolean;
  labelCol?: ColProps;
  wrapperCol?: ColProps;
}) => {
  const [configForm] = Form.useForm();
  const {
    protocolEndpoint,
    appId,
    labelCol = {
      span: 6,
    },
    wrapperCol = {
      span: 14,
    },
  } = props;
  const intl = useIntl();
  const { styles } = useStyles();

  useAsyncEffect(async () => {
    configForm.setFieldsValue(protocolEndpoint);
  }, [appId, protocolEndpoint]);

  return (
    <ProForm
      layout={'horizontal'}
      labelCol={labelCol}
      wrapperCol={wrapperCol}
      labelAlign={'right'}
      submitter={false}
      labelWrap
      form={configForm}
      className={styles.config}
    >
      <ProFormText
        label={intl.formatMessage({
          id: 'pages.app.config.detail.protocol_config.form.config_about.idp_sso_endpoint',
        })}
        name={'idpSsoEndpoint'}
        extra={intl.formatMessage({
          id: 'pages.app.config.detail.protocol_config.form.config_about.idp_sso_endpoint.extra',
        })}
        readonly
        proFieldProps={{
          render: (value: string) => {
            return value && <Typography.Text copyable>{value}</Typography.Text>;
          },
        }}
        fieldProps={{ autoComplete: 'off' }}
      />
    </ProForm>
  );
};
