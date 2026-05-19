/*
 * eiam-console - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
import { Settings as LayoutSettings } from '@ant-design/pro-components';

const Settings: LayoutSettings & {
  logo?: string;
  siderWidth: number;
} = {
  navTheme: 'light',
  colorPrimary: '#1677FF',
  layout: 'mix',
  contentWidth: 'Fluid',
  fixedHeader: true,
  fixSiderbar: true,
  splitMenus: false,
  colorWeak: false,
  title: '统一登录平台',
  logo: '/logo.svg',
  siderWidth: 215,
};

export default Settings;
