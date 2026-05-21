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
import { getChildOrganization, getRootOrganization } from '@/services/account';
import { useAsyncEffect } from 'ahooks';
import { Cascader } from 'antd';
import type { CascaderProps, DefaultOptionType } from 'antd/es/cascader';
import { useState } from 'react';

export type OrganizationCascaderProps = Omit<
  CascaderProps<DefaultOptionType>,
  'options' | 'loadData' | 'fieldNames' | 'changeOnSelect' | 'onChange'
>;

const OrgCascader = (props?: OrganizationCascaderProps) => {
  const [options, setOptions] = useState<any>();
  const [loading, setLoading] = useState<boolean>(false);

  useAsyncEffect(async () => {
    setLoading(true);
    const { success, result } =
      (await getRootOrganization().finally(() => {
        setLoading(false);
      })) || {};
    if (success && result) {
      setOptions([result]);
    }
  }, []);

  const loadData = async (selectedOptions: string | any[]) => {
    setLoading(true);
    const targetOption = selectedOptions[selectedOptions.length - 1];
    targetOption.loading = true; // load options lazily
    // 查询子节点
    const { success, result } = await getChildOrganization(targetOption.id).finally(() => {
      setLoading(false);
    });
    if (success && result) {
      targetOption.children = [...result];
      setOptions([...options]);
    }
    targetOption.loading = false;
  };

  return (
    <Cascader
      fieldNames={{ value: 'id', label: 'name' }}
      options={options}
      loadData={loadData}
      loading={loading}
      changeOnSelect
      showCheckedStrategy={'SHOW_CHILD'}
      {...props}
    />
  );
};

export default OrgCascader;
