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
import { ProFormSelect, ProFormSelectProps } from '@ant-design/pro-components';
import { PhoneAreaCode } from './data.d';

export default (props: Omit<ProFormSelectProps<PhoneAreaCode>, 'request'>) => {
  const { initialValue = '+86' } = props;

  return (
    <ProFormSelect
      {...props}
      initialValue={initialValue}
      request={async () => {
        return [{ value: '+86', label: `+86 中国` }];
      }}
    />
  );
};
