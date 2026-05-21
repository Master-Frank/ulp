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
import { Typography } from 'antd';

const { Text } = Typography;

const EllipsisMiddle = (props: { suffixCount: number; text: string }) => {
  const { suffixCount, text } = props;
  const start = text.slice(0, text.length - suffixCount).trim();
  const suffix = text.slice(-suffixCount).trim();
  return (
    <Text style={{ maxWidth: '100%' }} ellipsis={{ suffix, tooltip: true }} copyable={!!start}>
      {start ? start : '-'}
    </Text>
  );
};
export default EllipsisMiddle;
