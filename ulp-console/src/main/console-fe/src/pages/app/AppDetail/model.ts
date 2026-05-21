/*
 * eiam-console - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
import { useState } from 'react';
import { GetApp } from './data.d';

export default function () {
  //APP 信息
  const [app, setApp] = useState<GetApp>();
  return {
    app: app,
    setApp: setApp,
  };
}
