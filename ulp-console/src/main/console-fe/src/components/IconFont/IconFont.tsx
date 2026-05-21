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
import classnames from 'classnames';
import useStyle from './style';

type IconFontProps = {
  name: string;
  className?: string;
};
const prefixCls = 'topiam-icon';

/**
 * Icon Font
 * https://www.iconfont.cn/help/detail?helptype=code
 */
export default (props: IconFontProps) => {
  const { name, className } = props;
  const { wrapSSR, hashId } = useStyle(prefixCls);

  return wrapSSR(
    <svg className={className || classnames(`${prefixCls}`, hashId)} aria-hidden="true">
      <use xlinkHref={`#${name}`} />
    </svg>,
  );
};
