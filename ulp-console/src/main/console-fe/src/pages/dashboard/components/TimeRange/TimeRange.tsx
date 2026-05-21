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
import { Segmented } from 'antd';
import { useState } from 'react';
import { getTimeDistance } from './utils';
import dayjs from 'dayjs';
import useStyle from './style';
import type { SegmentedValue } from 'antd/es/segmented';
import { useAsyncEffect, useMount } from 'ahooks';
import { useIntl } from '@umijs/max';

export type Type = 'today' | 'week' | 'month' | 'year' | SegmentedValue;

interface TimeRangeProps {
  /**
   * 类型
   */
  type: Type;
  /**
   * onChange
   *
   * @param startTime
   * @param endTime
   */
  onChange: (startTime: string, endTime: string) => void;
}

/**
 * 日期选择器组件
 *
 * @param props
 */
export default (props: TimeRangeProps) => {
  const { onChange, type } = props;
  const [segmentedType, setSegmentedType] = useState<Type>();
  const { styles } = useStyle();
  const intl = useIntl();

  useMount(async () => {
    setSegmentedType(type);
  });

  useAsyncEffect(async () => {
    if (segmentedType) {
      const timeDistance = getTimeDistance(segmentedType);
      onChange(
        dayjs(timeDistance?.[0]).format('YYYY-MM-DD HH:mm:ss'),
        dayjs(timeDistance?.[1]).format('YYYY-MM-DD HH:mm:ss'),
      );
    }
  }, [segmentedType]);

  return (
    <div className={styles.main}>
      <div className={'sales-extra-wrap'}>
        <div className={'sales-extra'}>
          <Segmented
            defaultValue={segmentedType}
            options={[
              { label: intl.formatMessage({ id: 'app.today' }), value: 'today' },
              { label: intl.formatMessage({ id: 'app.week' }), value: 'week' },
              { label: intl.formatMessage({ id: 'app.month' }), value: 'month' },
              { label: intl.formatMessage({ id: 'app.year' }), value: 'year' },
            ]}
            onChange={(value) => {
              setSegmentedType(value);
            }}
          />
        </div>
      </div>
    </div>
  );
};
