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
import { Button } from 'antd';
import useStyle from './style';
import { useIntl } from '@@/exports';

const prefixCls = 'topiam-forget-password';

const Success = ({ close }: { close: () => void }) => {
  const { styles } = useStyle(prefixCls);
  const intl = useIntl();
  return (
    <div className={styles.main}>
      <div className={`${prefixCls}-success`}>
        <div className={`${prefixCls}-success-title`}>
          {intl.formatMessage({ id: 'pages.login.forget-password.success' })}
        </div>
        <div className={`${prefixCls}-success-desc`}>
          {intl.formatMessage({ id: 'pages.login.forget-password.success.desc' })}
        </div>
        <Button
          type="primary"
          className={`${prefixCls}-success-button`}
          size={'large'}
          onClick={() => close()}
        >
          {intl.formatMessage({ id: 'app.return' })}
        </Button>
      </div>
    </div>
  );
};
export default Success;
