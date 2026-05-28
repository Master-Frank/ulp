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
import IconFont from '@/components/IconFont';
import type { ReactNode } from 'react';

/**
 * ICON
 */
export const ICON_LIST = {
  password: <IconFont name="icon-password" />,
  dingtalk: <IconFont name="icon-dingtalk" />,
  feishu: <IconFont name="icon-feishu" />,
  wechat_work: <IconFont name="icon-qiyeweixin" />,
  wechat_oauth: <IconFont name="icon-weixin" />,
  dingtalk_oauth: <IconFont name="icon-dingtalk" />,
  wechatwork_oauth: <IconFont name="icon-qiyeweixin" />,
  feishu_oauth: <IconFont name="icon-feishu" />,
  qq_oauth: <IconFont name="icon-qq" />,
  gitee_oauth: <IconFont name="icon-gitee" />,
  weibo_oauth: <IconFont name="icon-weibo" />,
  github_oauth: <IconFont name="icon-github" />,
  google_oauth: <IconFont name="icon-google" />,
  alipay_oauth: <IconFont name="icon-alipay" />,
  app_jwt: <IconFont name="icon-jwt" />,
  oidc: <IconFont name="icon-openid" />,
  app_form: <IconFont name="icon-form" />,
} as Record<string, ReactNode>;
