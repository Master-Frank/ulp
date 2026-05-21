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
import { Collapse, Typography } from 'antd';
import Paragraph from 'antd/es/typography/Paragraph';
import type { UserType } from '../../data.d';
import { AuditList, EventStatus } from '../../data.d';
import useStyles from './style';
import { useIntl } from '@umijs/max';
import dayjs from 'dayjs';

const { Text } = Typography;

interface ExpandedCardProps {
  record: AuditList;
  index: number;
  type: UserType;
}
const ExpandedCard = (props: ExpandedCardProps) => {
  const { record } = props;
  const { styles } = useStyles();
  const intl = useIntl();
  const USER_TYPE = {
    user: intl.formatMessage({ id: 'pages.audit.columns.user_type.user' }),
    admin: intl.formatMessage({ id: 'pages.audit.columns.user_type.admin' }),
  };

  const outputRecord = (text: string) => {
    if (!text || text === 'null' || text === '-' || text === 'undefined') {
      return '-';
    } else {
      return text;
    }
  };

  return (
    <div className={styles.expandedCard}>
      {/*用户相关*/}
      <div className={styles.expandedCardLineUser}>
        <div className={styles.expandedCardEachLine}>
          <span className={styles.expandedCardLabel}>
            {intl.formatMessage({ id: 'pages.audit.columns.user_id' })}
          </span>
          <span className={styles.expandedCardLabel}>
            <Paragraph
              style={{
                marginBottom: 0,
              }}
              className={styles.expandedCardContent}
              copyable
            >
              {outputRecord(record.userId)}
            </Paragraph>
          </span>
        </div>
        <div className={styles.expandedCardEachLine}>
          <span className={styles.expandedCardLabel}>
            {intl.formatMessage({ id: 'pages.audit.columns.username' })}
          </span>
          <span className={styles.expandedCardContent}>{outputRecord(record.username)}</span>
        </div>
        <div className={styles.expandedCardEachLine}>
          <span className={styles.expandedCardLabel}>
            {intl.formatMessage({ id: 'pages.audit.columns.user_type' })}
          </span>
          <span className={styles.expandedCardContent}>{USER_TYPE[record.userType]}</span>
        </div>
      </div>
      {/*地理位置、客户端相关*/}
      <div className={styles.expandedCardLineGeo}>
        <div className={styles.expandedCardEachLine}>
          <span className={styles.expandedCardLabel}>
            {intl.formatMessage({ id: 'pages.audit.columns.device_type' })}
          </span>
          <span className={styles.expandedCardContent}>
            {(record.userAgent && record.userAgent?.deviceType) || '-'}
          </span>
        </div>
        <div className={styles.expandedCardEachLine}>
          <span className={styles.expandedCardLabel}>
            {intl.formatMessage({ id: 'pages.audit.columns.operate_system' })}
          </span>
          <span className={styles.expandedCardContent}>
            {(record.userAgent && record.userAgent?.platform) || '-'}
          </span>
        </div>
        <div className={styles.expandedCardEachLine}>
          <span className={styles.expandedCardLabel}>
            {intl.formatMessage({ id: 'pages.audit.columns.client_ip' })}
          </span>
          <span className={styles.expandedCardContent}>{record.geoLocation?.ip || '-'}</span>
        </div>
        <div className={styles.expandedCardEachLine}>
          <span className={styles.expandedCardLabel}>
            {intl.formatMessage({ id: 'pages.audit.columns.browser' })}
          </span>
          <span className={styles.expandedCardContent}>
            {(record.userAgent && record.userAgent?.browser) || '-'}
          </span>
        </div>
        <div className={styles.expandedCardEachLine}>
          <span className={styles.expandedCardLabel}>
            {intl.formatMessage({ id: 'pages.audit.columns.country' })}
          </span>
          <span className={styles.expandedCardContent}>
            {(record.geoLocation && record.geoLocation?.countryName) || '-'}
          </span>
        </div>
        <div className={styles.expandedCardEachLine}>
          <span className={styles.expandedCardLabel}>
            {intl.formatMessage({ id: 'pages.audit.columns.province' })}
          </span>
          <span className={styles.expandedCardContent}>
            {(record.geoLocation && record.geoLocation?.provinceName) || '-'}
          </span>
        </div>
        <div className={styles.expandedCardEachLine}>
          <span className={styles.expandedCardLabel}>
            {intl.formatMessage({ id: 'pages.audit.columns.city' })}
          </span>
          <span className={styles.expandedCardContent}>
            {(record.geoLocation && record.geoLocation?.cityName) || '-'}
          </span>
        </div>
      </div>
      {/*事件相关*/}
      <div className={styles.expandedCardLineEvent}>
        <div className={styles.expandedCardEachLine}>
          <span className={styles.expandedCardLabel}>
            {intl.formatMessage({ id: 'pages.audit.columns.event_time' })}
          </span>
          <span className={styles.expandedCardContent}>
            <Text style={{ maxWidth: '100%' }} ellipsis={true}>
              {dayjs(record.eventTime).format('YYYY-MM-DD HH:mm:ss')}
            </Text>
          </span>
        </div>
        <div className={styles.expandedCardEachLine}>
          <span className={styles.expandedCardLabel}>
            {intl.formatMessage({ id: 'pages.audit.columns.event_type' })}
          </span>
          <span className={styles.expandedCardContent}>{record.eventType}</span>
        </div>
        <div className={styles.expandedCardEachLine}>
          <span className={styles.expandedCardLabel}>
            {intl.formatMessage({ id: 'pages.audit.columns.event_status' })}
          </span>
          <span className={styles.expandedCardContent}>
            {record.eventStatus === EventStatus.success && (
              <span style={{ color: '#87d068' }}>{intl.formatMessage({ id: 'app.success' })}</span>
            )}
            {record.eventStatus === EventStatus.fail && (
              <span style={{ color: '#e54545' }}>{intl.formatMessage({ id: 'app.fail' })}</span>
            )}
          </span>
        </div>
      </div>
      {/*操作对象*/}
      {record.targets && (
        <div className={styles.expandedCardTarget}>
          <div className={styles.expandedCardEachLine}>
            <Collapse
              ghost
              items={[
                {
                  key: '1',
                  label: intl.formatMessage({ id: 'pages.audit.columns.event_object' }),
                  className: styles.expandedCardPanel,
                  children: (
                    <>
                      {record.targets.map((i: any) => {
                        const items = () => {
                          if (i?.name) {
                            return [
                              {
                                label: i.name,
                                key: i.id || i.type,
                                className: styles.expandedCardPanel,
                                children: (
                                  <div className={styles.expandedCardPanelContent}>
                                    <p>
                                      {intl.formatMessage({
                                        id: 'pages.audit.columns.event_object.id',
                                      })}
                                      {i.id}
                                    </p>
                                    <p>
                                      {intl.formatMessage({
                                        id: 'pages.audit.columns.event_object.type',
                                      })}
                                      {i.typeName}
                                    </p>
                                  </div>
                                ),
                              },
                            ];
                          }
                          return [
                            {
                              label: i.typeName,
                              key: i.type,
                              className: styles.expandedCardPanel,
                              children: (
                                <div className={styles.expandedCardPanelContent}>
                                  <p>
                                    {intl.formatMessage({
                                      id: 'pages.audit.columns.event_object.type',
                                    })}
                                    {i.typeName}
                                  </p>
                                </div>
                              ),
                            },
                          ];
                        };
                        return <Collapse ghost key={i.id || i.type} items={items()} />;
                      })}
                    </>
                  ),
                },
              ]}
            ></Collapse>
          </div>
        </div>
      )}
    </div>
  );
};
export default ExpandedCard;
