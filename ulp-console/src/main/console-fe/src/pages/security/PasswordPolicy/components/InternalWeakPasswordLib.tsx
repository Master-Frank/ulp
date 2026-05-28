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
import { getWeakPasswordLib } from '../service';
import { useAsyncEffect } from 'ahooks';
import { List, Modal, Spin } from 'antd';
import * as React from 'react';
import { useState } from 'react';
import { useIntl } from '@umijs/max';

export type InternalWeakCipherProps = {
  visible: boolean;
  onCancel?: (e: React.MouseEvent<HTMLElement>) => void;
};
export default (props: InternalWeakCipherProps) => {
  const { visible, onCancel } = props;
  const [data, setData] = useState<string[]>();
  const [loading, setLoading] = useState<boolean>(true);
  const intl = useIntl();
  /** useAsyncEffect */
  useAsyncEffect(async () => {
    if (visible) {
      setLoading(true);
      const { success, result } = await getWeakPasswordLib();
      if (success && result) {
        const libraries = result?.map((value) => {
          return value.value;
        });
        setData(libraries);
      }
      setLoading(false);
    }
  }, [visible]);
  return (
    <Modal
      open={visible}
      title={intl.formatMessage({
        id: 'pages.setting.security.password_policy.weak_password_checking.password_library',
      })}
      footer={false}
      onCancel={onCancel}
      destroyOnClose
    >
      <Spin spinning={loading}>
        <List
          size="small"
          bordered={false}
          dataSource={data}
          pagination={{ simple: true }}
          renderItem={(item) => <List.Item>{item}</List.Item>}
        />
      </Spin>
    </Modal>
  );
};
