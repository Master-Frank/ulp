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
import type { ProColumns } from '@ant-design/pro-components';
import { ProTable } from '@ant-design/pro-components';

import { Badge, Space, Tag } from 'antd';
import { useEffect } from 'react';
import { EventStatus } from '@/pages/audit/data.d';
import { getLoginAuditList } from '@/services/account';
import { useIntl } from '@umijs/max';

export default (props: { userId: string }) => {
  const { userId } = props;
  useEffect(() => {}, []);
  const intl = useIntl();

  /**
   * columns
   */
  const columns: ProColumns<AccountAPI.UserLoginAuditList>[] = [
    {
      title: intl.formatMessage({ id: 'pages.account.user_detail.login_audit.columns.app_name' }),
      ellipsis: true,
      search: false,
      dataIndex: 'appName',
      fixed: 'left',
    },
    {
      title: intl.formatMessage({ id: 'pages.account.user_detail.login_audit.columns.client_ip' }),
      dataIndex: 'clientIp',
      ellipsis: true,
      valueType: 'text',
      search: false,
      render: (_, item) => {
        return item.clientIp ? (
          <Badge status={'success'} text={item.clientIp} />
        ) : (
          <Badge status={'error'} text={intl.formatMessage({ id: 'app.unknown' })} />
        );
      },
    },
    {
      title: intl.formatMessage({ id: 'pages.account.user_detail.login_audit.columns.platform' }),
      ellipsis: true,
      dataIndex: 'platform',
      width: 110,
      search: false,
    },
    {
      title: intl.formatMessage({ id: 'pages.account.user_detail.login_audit.columns.browser' }),
      dataIndex: 'browser',
      search: false,
    },
    {
      title: intl.formatMessage({ id: 'pages.account.user_detail.login_audit.columns.location' }),
      dataIndex: 'location',
      search: false,
    },
    {
      title: intl.formatMessage({ id: 'pages.account.user_detail.login_audit.columns.event_time' }),
      dataIndex: 'eventTime',
      sorter: true,
      valueType: 'dateTime',
      search: false,
      ellipsis: true,
    },
    {
      title: intl.formatMessage({
        id: 'pages.account.user_detail.login_audit.columns.event_status',
      }),
      dataIndex: 'eventStatus',
      search: false,
      align: 'center',
      render: (_text: any, row: any) => (
        <Space>
          {row.eventStatus === EventStatus.success && (
            <Tag color="#87d068">{intl.formatMessage({ id: 'app.success' })}</Tag>
          )}
          {row.eventStatus === EventStatus.fail && (
            <Tag color="#f50">{intl.formatMessage({ id: 'app.fail' })}</Tag>
          )}
        </Space>
      ),
    },
  ];

  return (
    <>
      <ProTable
        columns={columns}
        search={false}
        rowKey={'id'}
        scroll={{ x: 900 }}
        request={getLoginAuditList}
        params={{ userId: userId }}
        pagination={{ pageSize: 10 }}
      />
    </>
  );
};
