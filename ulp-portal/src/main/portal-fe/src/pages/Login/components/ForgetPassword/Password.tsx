/*
 * ulp-portal - United Login Platform
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
import { ProForm, ProFormProps, ProFormText } from '@ant-design/pro-components';
import { LockTwoTone } from '@ant-design/icons';
import { FormattedMessage, useIntl } from '@@/exports';
import useStyle from './style';
import Title from '@/components/Title';
import { Typography } from 'antd';

const prefixCls = 'ulp-forget-password';
const { Paragraph } = Typography;

type PasswordProps = {
  phone: string;
};
const Password = ({ phone, ...rest }: PasswordProps & ProFormProps) => {
  const intl = useIntl();
  const { styles } = useStyle(prefixCls);
  return (
    <div className={styles.main}>
      <Title size={'h1'} title={intl.formatMessage({ id: 'pages.login.forget-password' })} />
      <Paragraph>{`${intl.formatMessage(
        { id: 'pages.login.forget-password.password.desc' },
        { phone: phone },
      )}`}</Paragraph>
      <ProForm {...rest}>
        <ProFormText.Password
          name="newPassword"
          fieldProps={{
            size: 'large',
            autoComplete: 'off',
            prefix: <LockTwoTone className={'icon'} />,
          }}
          placeholder={intl.formatMessage({
            id: 'pages.login.forget-password.password.new',
          })}
          rules={[
            {
              required: true,
              message: <FormattedMessage id="pages.login.password.required" />,
            },
          ]}
        />
        <ProFormText.Password
          name="passwordAgain"
          fieldProps={{
            size: 'large',
            autoComplete: 'off',
            prefix: <LockTwoTone className={'icon'} />,
          }}
          placeholder={intl.formatMessage({
            id: 'pages.login.forget-password.password.confirm',
          })}
          validateTrigger={'onBlur'}
          rules={[
            {
              required: true,
              message: <FormattedMessage id="pages.login.password.required" />,
            },
            {
              validator: (_, value, callback) => {
                const password = rest?.formRef?.current?.getFieldValue('newPassword');
                if (password && password !== value) {
                  callback(
                    intl.formatMessage({
                      id: 'pages.login.forget-password.inconsistency',
                    }),
                  );
                }
                callback();
              },
              message: <FormattedMessage id="pages.login.forget-password.inconsistency" />,
            },
          ]}
        />
      </ProForm>
    </div>
  );
};
export default Password;
