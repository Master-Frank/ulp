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
import { Modal } from 'antd';
import React from 'react';

export type MailTemplateBrowseProps = {
  /** 标题 */
  title: string | React.ReactNode;
  /** 是否显示 */
  visible: boolean;
  /** 内容 */
  content: string;
  /** 取消方法 */
  onCancel: () => void;
};
export default (props: MailTemplateBrowseProps) => {
  const { title, visible, onCancel, content } = props;
  return (
    <Modal
      title={title}
      open={visible}
      closable
      destroyOnClose
      width="800px"
      onCancel={onCancel}
      onOk={onCancel}
    >
      {/* eslint-disable-next-line react/no-danger */}
      <div dangerouslySetInnerHTML={{ __html: content }} />
    </Modal>
  );
};
