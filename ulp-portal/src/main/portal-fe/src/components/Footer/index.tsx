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
// import { GithubOutlined } from '@ant-design/icons';
import type { FooterProps } from '@ant-design/pro-components';
import { DefaultFooter } from '@ant-design/pro-components';

const currentYear = new Date().getFullYear();

export default (props: FooterProps) => (
  <DefaultFooter
    style={{ backgroundColor: 'transparent' }}
    copyright={`Copyright 2005-${currentYear} Charles有限公司`}
    links={[
      {
        key: 'website',
        title: '官方网站',
        href: 'https://baidu.com',
        blankTarget: true,
      },
      // {
      //   key: 'github',
      //   title: <GithubOutlined />,
      //   href: 'https://github.com/',
      //   blankTarget: true,
      // },
      // {
      //   key: 'docs',
      //   title: '使用文档',
      //   href: 'https://eiam.topiam.cn/docs/introduction/overview',
      //   blankTarget: true,
      // },
    ]}
    {...props}
  />
);
