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
import { history } from '@@/core/history';
import { PageContainer } from '@ant-design/pro-components';
import React, { useEffect, useState } from 'react';
import MailProvider from '../Message/components/MailProvider';
import MailTemplate from '../Message/components/MailTemplate';
import SmsProvider from '../Message/components/SmsProvider';
import { TabType } from './constant';
import queryString from 'query-string';
import { useLocation } from '@umijs/max';
import { useIntl } from '@@/exports';

export default (): React.ReactNode => {
  const [activeKey, setActiveKey] = useState<string>();
  const location = useLocation();
  const intl = useIntl();
  useEffect(() => {
    const query = queryString.parse(location.search);
    const { type } = query as {
      type: TabType;
    };
    if (!type || !TabType[type]) {
      setActiveKey(TabType.mail_template);
      history.replace({
        pathname: location.pathname,
        search: queryString.stringify({ type: TabType.mail_template }),
      });
      return;
    }
    setActiveKey(type);
  }, [activeKey]);
  return (
    <PageContainer
      content={intl.formatMessage({ id: 'pages.setting.message.desc' })}
      tabActiveKey={activeKey}
      onTabChange={(key: string) => {
        setActiveKey(key);
        history.replace({
          pathname: location.pathname,
          search: queryString.stringify({ type: key }),
        });
      }}
      tabList={[
        {
          tab: intl.formatMessage({ id: 'pages.setting.message.sms_template' }),
          key: TabType.mail_template,
        },
        {
          tab: intl.formatMessage({ id: 'pages.setting.message.mail' }),
          key: TabType.mail,
        },
        {
          tab: intl.formatMessage({ id: 'pages.setting.message.sms' }),
          key: TabType.sms,
        },
      ]}
    >
      {/*邮件*/}
      {activeKey === TabType.mail && <MailProvider visible={activeKey === TabType.mail} />}
      {/*邮件模版*/}
      {activeKey === TabType.mail_template && (
        <MailTemplate visible={activeKey === TabType.mail_template} />
      )}
      {/*短信*/}
      {activeKey === TabType.sms && <SmsProvider visible={activeKey === TabType.sms} />}
    </PageContainer>
  );
};
