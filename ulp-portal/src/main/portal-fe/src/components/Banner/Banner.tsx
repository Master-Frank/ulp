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
import { Alert } from 'antd';
import React from 'react';
import Marquee from 'react-fast-marquee';

const message = () => {
  return (
    <>
      <span>如果使用中遇到问题，请联系Frank，你的支持将是我们前行的动力。
      </span>
    </>
  );
};
export default (props: { play?: boolean }) => {
  const { play } = props;
  return (
    <Alert
      style={{
        padding: 0,
        background:
          'repeating-linear-gradient(35deg, hsl(196 120% 85%), hsl(196 120% 85%) 20px, hsl(196 120% 95%) 10px, hsl(196 120% 95%) 40px)',
      }}
      message={
        <div
          style={{
            whiteSpace: 'nowrap', //强制文本在一行内输出
            overflow: 'hidden', //隐藏溢出部分
            textOverflow: 'ellipsis', //对溢出部分加上...
            textAlign: 'center',
            color: 'black',
          }}
        >
          {play ? (
            <Marquee pauseOnHover gradient={false}>
              {message()}
            </Marquee>
          ) : (
            message()
          )}
        </div>
      }
      showIcon={false}
      banner
      type={'info'}
    />
  );
};
