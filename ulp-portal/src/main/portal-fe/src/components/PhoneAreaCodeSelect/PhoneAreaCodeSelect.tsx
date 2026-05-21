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
import { Select, Spin } from 'antd';
import { useAsyncEffect } from 'ahooks';
import { useState } from 'react';
import { PhoneAreaCode } from './data.d';
import { BaseOptionType, DefaultOptionType } from 'rc-select/lib/Select';

const { Option } = Select;

export interface PhoneAreaCodeProps<
  ValueType = any,
  OptionType extends BaseOptionType = DefaultOptionType,
> {
  defaultValue?: string;
  onChange?: (value: ValueType, option: OptionType | OptionType[]) => void;
}
export default (props: PhoneAreaCodeProps) => {
  const { defaultValue = '+86', onChange } = props;
  const [list, setList] = useState<PhoneAreaCode[]>();
  const [loading, setLoading] = useState<boolean>(true);
  useAsyncEffect(async () => {
    setLoading(true);
    setList([
      {
        englishName: 'China',
        chineseName: '中国',
        countryCode: 'CN',
        phoneCode: '+86',
      },
    ]);
    setLoading(false);
  }, []);
  return (
    <Spin spinning={loading}>
      <Select
        showSearch
        defaultValue={defaultValue}
        style={{ minWidth: '100px' }}
        onChange={onChange}
      >
        {list?.map((value) => {
          return (
            <Option value={value.phoneCode} key={value.countryCode}>
              {`${value.phoneCode} ${value.chineseName}`}
            </Option>
          );
        })}
      </Select>
    </Spin>
  );
};
