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
import { ProCard } from '@ant-design/pro-components';
import type { BarConfig } from '@ant-design/charts';
import { Bar } from '@ant-design/charts';
import { useState } from 'react';
import { useAsyncEffect } from 'ahooks';
import TimeRange from '../TimeRange';
import { getAppVisitRank } from '../../service';
import { Empty, Skeleton } from 'antd';
import { useIntl } from '@umijs/max';

export default () => {
  const [loading, setLoading] = useState<boolean>(false);
  const [data, setData] = useState<Record<string, any>[]>([]);
  const intl = useIntl();

  const [rangePickerValue, setRangePickerValue] = useState<{
    startTime: string;
    endTime: string;
  }>();

  useAsyncEffect(async () => {
    if (rangePickerValue) {
      const { startTime, endTime } = rangePickerValue;
      setLoading(true);
      const { result, success } = await getAppVisitRank(startTime, endTime);
      if (success) {
        setData(result);
      }
      //延迟展示
      setTimeout(() => {
        setLoading(false);
      }, 90);
    }
  }, [rangePickerValue]);

  const appRankConfig = {
    height: 290,
    minBarWidth: 25,
    maxBarWidth: 35,
    data: data,
    xField: 'count',
    yField: 'name',
    label: {
      position: 'middle',
      style: {
        fill: '#FFFFFF',
        opacity: 0.6,
      },
    },
    xAxis: {
      label: {
        autoHide: true,
        autoRotate: false,
      },
    },
    barWidthRatio: 0.8,
    meta: {
      name: {
        alias: intl.formatMessage({ id: 'pages.dashboard.analysis.app_visit_rank.name' }),
      },
      count: {
        alias: intl.formatMessage({ id: 'pages.dashboard.analysis.app_visit_rank.count' }),
      },
    },
  } as BarConfig;

  return (
    <ProCard
      title={intl.formatMessage({ id: 'pages.dashboard.analysis.app_visit_rank.title' })}
      headerBordered
      extra={
        <TimeRange
          type={'today'}
          onChange={(startTime, endTime) => {
            setRangePickerValue({ startTime, endTime });
          }}
        />
      }
    >
      <Skeleton loading={loading} style={{ height: 290 }} active paragraph={{ rows: 7 }}>
        {data.length > 0 ? (
          <Bar {...appRankConfig} />
        ) : (
          <div style={{ height: 290, alignItems: 'center', display: 'grid' }}>
            <Empty image={Empty.PRESENTED_IMAGE_SIMPLE} />
          </div>
        )}
      </Skeleton>
    </ProCard>
  );
};
