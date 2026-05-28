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
import { PageContainer } from '@ant-design/pro-components';
import React, { useState } from 'react';
import { useIntl } from '@umijs/max';
import { TabType } from './constant';
import DefensePolicy from './components/DefensePolicy';
import Basic from './components/Basic';

export default () => {
  const [tabActiveKey, setTabActiveKey] = useState<string>(TabType.basic);
  const intl = useIntl();

  return (
    <PageContainer
      tabActiveKey={tabActiveKey}
      onTabChange={(key) => {
        setTabActiveKey(key);
      }}
      tabList={[
        {
          key: TabType.basic,
          tab: intl.formatMessage({ id: 'pages.setting.basic_setting' }),
        },
        {
          key: TabType.defense_policy,
          tab: intl.formatMessage({ id: 'pages.setting.security.defense_policy' }),
        },
      ]}
    >
      {tabActiveKey === TabType.basic && <Basic />}
      {tabActiveKey === TabType.defense_policy && <DefensePolicy />}
    </PageContainer>
  );
};
