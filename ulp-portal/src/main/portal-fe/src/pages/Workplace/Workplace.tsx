/*
 * eiam-portal - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
import { Alert, Card } from 'antd';
import type { FC } from 'react';

import { PageContainer } from '@ant-design/pro-components';

const Workplace: FC = () => {
  return (
    <PageContainer>
      <Card>
        <Alert banner description={'欢迎使用 DLP 统一登录平台'} type={'success'} />
      </Card>
    </PageContainer>
  );
};

export default Workplace;
