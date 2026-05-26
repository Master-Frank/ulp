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
import { App, Image } from 'antd';
import React from 'react';
import { useAsyncEffect } from 'ahooks';

type IProps = {
  children: React.JSX.Element;
};

const PageContainer: React.FC<IProps> = (props) => {
  const { notification } = App.useApp();

  useAsyncEffect(async () => {
    notification.open({
      key: 'notification',
      message: '提示',
      duration: null,
      placement: 'bottomRight',
      description: (
        <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
          <Image src={'/wecomqrcode.jpg'} width={180} preview={false} />
          <div>
            <span style={{ color: '#1890FF', textAlign: 'center', display: 'block' }}>
              如果使用中遇到问题，联系Frank
            </span>
          </div>
        </div>
      ),
      style: {
        width: 220,
        marginBottom: 0,
        padding: 15,
      },
    });
  }, []);
  return <>{props.children}</>;
};
export default PageContainer;
