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
import { AppProtocolType } from '@/constant';

import { useAsyncEffect } from 'ahooks';
import { Skeleton } from 'antd';
import { useState } from 'react';
import FromConfig from './FromProtocolConfig';
import JwtConfig from './JwtProtocolConfig';
import OidcConfig from './OidcProtocolConfig';
import { GetApp } from '../../data.d';
import { useModel } from '@@/exports';

export default () => {
  const { app } = useModel('app.AppDetail.model');
  const [loading, setLoading] = useState<boolean>(true);
  useAsyncEffect(async () => {
    setLoading(true);

    setLoading(false);
  }, [app]);

  const ComponentByKey = ({ key, app }: { key: string; app: GetApp }) => {
    const components = {
      [AppProtocolType.jwt]: JwtConfig,
      [AppProtocolType.oidc]: OidcConfig,
      [AppProtocolType.form]: FromConfig,
    };
    const Component = components[key];
    return <Component app={app} />;
  };
  return (
    <Skeleton loading={loading} active={true} paragraph={{ rows: 5 }}>
      {app && ComponentByKey({ key: app?.protocol, app: app })}
    </Skeleton>
  );
};
