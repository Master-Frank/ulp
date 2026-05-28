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
import { Col, Row } from 'antd';
import { useState } from 'react';
import OrgTree from './components/Organization';
import UserList from './components/User';
import { useIntl } from '@umijs/max';

/**左侧布局*/
const leftLayout = { xxl: 5, lg: 6, md: 24, sm: 24, xs: 24 };
/**右侧布局*/
const rightLayout = { xxl: 19, lg: 18, md: 24, sm: 24, xs: 24 };
/**
 * 组织&用户
 */
export const User = () => {
  const [organization, setOrganization] = useState<{
    id: string | number;
    name: string;
  }>();

  /**
   * tree select
   *
   * @param id
   * @param name
   */
  const treeOnSelect = (id: string | number, name: string) => {
    setOrganization({ id: id, name });
  };
  const intl = useIntl();

  return (
    <PageContainer content={intl.formatMessage({ id: 'pages.account.user_list.desc' })}>
      <Row gutter={[16, 16]}>
        {/* 左侧 */}
        <Col {...leftLayout} style={{ minHeight: '100%', overflow: 'auto' }}>
          <OrgTree onSelect={treeOnSelect} />
        </Col>
        {/* 表格 */}
        <Col {...rightLayout} style={{ minHeight: '100%', overflow: 'auto' }}>
          <UserList organization={organization} />
        </Col>
      </Row>
    </PageContainer>
  );
};
export default () => {
  return <User />;
};
