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
import React from 'react';
import { Col, Divider, Image, Modal, Row, Space, Typography } from 'antd';
import { CopyrightOutlined, InfoCircleFilled } from '@ant-design/icons';
import { FormattedMessage } from '@umijs/max';
import { useSafeState } from 'ahooks';
import { createStyles } from 'antd-style';

const { Text } = Typography;

const useStyle = createStyles({
  main: {
    padding: '10px 0',
  },
});
const About: React.FC = () => {
  const { styles } = useStyle();
  const currentYear = new Date().getFullYear();
  const [aboutOpen, setAboutOpen] = useSafeState(false);

  return (
    <>
      <InfoCircleFilled
        key="InfoCircleFilled"
        onClick={() => {
          setAboutOpen(true);
        }}
      />
      <Modal
        title={<FormattedMessage id={'component.right_content.about.title'} />}
        open={aboutOpen}
        centered
        footer={null}
        width={650}
        onCancel={() => setAboutOpen(false)}
      >
        <div className={styles.main}>
          <Row gutter={16}>
            <Col span={6} style={{ borderRight: '3px solid #f0f2f5' }}>
              <Image src={'/logo.svg'} preview={false} />
              <Divider dashed style={{ marginBottom: 10, marginTop: 10 }} />
              <Image src={'/wecomqrcode.jpg'} preview={false} />
              <Text style={{ textAlign: 'center', display: 'block' }}>企微二维码</Text>
            </Col>
            <Col span={18}>
              <Space direction="vertical" size={'middle'}>
                <Text>产品：ULP 统一登录平台</Text>
                <Text>版本：1.0.0</Text>
                <Text>
                  Copyright <CopyrightOutlined /> 2022-{currentYear} Frank Zhang. Licensed under
                  the Apache License, Version 2.0.
                </Text>
              </Space>
            </Col>
          </Row>
        </div>
      </Modal>
    </>
  );
};
export default About;
