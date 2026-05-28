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
import { GridContent } from '@ant-design/pro-components';
import { Col, Row } from 'antd';
import Overview from './components/Overview';
import HotAuthnProvider from './components/HotAuthnProvider';
import AuthnZone from './components/AuthnZone';
import AppVisitRank from './components/AppVisitRank';
import AuthnQuantity from './components/AuthnQuantity';

const Dashboard = () => {
  return (
    <GridContent style={{ height: '100%' }}>
      {/*概述*/}
      <Overview />
      <Row gutter={[24, 24]}>
        <Col
          {...{
            xs: 24,
            sm: 24,
            md: 24,
            lg: 12,
            xl: 12,
            style: { marginBottom: 24 },
          }}
        >
          {/*授权数量*/}
          <AuthnQuantity />
        </Col>
        <Col
          {...{
            xs: 24,
            sm: 24,
            md: 24,
            lg: 12,
            xl: 12,
            style: { marginBottom: 24 },
          }}
        >
          {/*访问量统计*/}
          <AppVisitRank />
        </Col>
      </Row>
      <Row gutter={[24, 24]}>
        <Col
          {...{
            xs: 24,
            sm: 24,
            md: 24,
            lg: 12,
            xl: 12,
            style: { marginBottom: 24 },
          }}
        >
          {/*热点提供商*/}
          <HotAuthnProvider />
        </Col>
        <Col
          {...{
            xs: 24,
            sm: 24,
            md: 24,
            lg: 12,
            xl: 12,
            style: { marginBottom: 24 },
          }}
        >
          {/*认证区域*/}
          <AuthnZone />
        </Col>
      </Row>
    </GridContent>
  );
};
export default () => {
  return <Dashboard />;
};
