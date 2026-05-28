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
import { FormattedMessage, useIntl } from '@@/plugin-locale/localeExports';
import { LockTwoTone, UserOutlined } from '@ant-design/icons';
import { ProFormText } from '@ant-design/pro-components';
import { createStyles } from 'antd-style';

const useStyle = createStyles(({ token }) => {
  return {
    main: {
      ['.icon']: {
        color: token.colorPrimary,
        fontSize: token.fontSize,
      },
    },
  };
});
export default () => {
  const intl = useIntl();
  const { styles } = useStyle();
  return (
    <div className={styles.main}>
      <ProFormText
        name="username"
        fieldProps={{
          size: 'large',
          prefix: <UserOutlined className={'icon'} />,
          autoComplete: 'off',
        }}
        placeholder={intl.formatMessage({
          id: 'pages.login.username.placeholder',
        })}
        rules={[
          {
            required: true,
            message: <FormattedMessage id="pages.login.username.required" />,
          },
        ]}
      />
      <ProFormText.Password
        name="password"
        fieldProps={{
          size: 'large',
          autoComplete: 'off',
          prefix: <LockTwoTone className={'icon'} />,
        }}
        placeholder={intl.formatMessage({
          id: 'pages.login.password.placeholder',
        })}
        rules={[
          {
            required: true,
            message: <FormattedMessage id="pages.login.password.required" />,
          },
        ]}
      />
    </div>
  );
};
