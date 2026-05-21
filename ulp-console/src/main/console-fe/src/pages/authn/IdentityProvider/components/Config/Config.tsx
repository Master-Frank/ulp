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
import { ProFormSwitch } from '@ant-design/pro-components';
import { IdentityProviderType } from '../../constant';
import DingTalkOAuthConfig from './DingTalkOAuthConfig';
import FeiShuOAuthConfig from './FeiShuOAuthConfig';
import QqOAuthConfig from './QqOAuthConfig';
import WeChatOAuthConfig from './WeChatOAuthConfig';
import WeChatWorkOAuthConfig from './WeChatWorkOAuthConfig';
import GithubOAuthConfig from './GithubOAuthConfig';
import GiteeOAuthConfig from './GiteeOAuthConfig';
import AliPayOAuthConfig from './AliPayOAuthConfig';
import { useIntl } from '@umijs/max';

/**
 * Config
 *
 * @param props
 * @constructor
 */
const Config = (props: { type: IdentityProviderType | string; isCreate?: boolean }) => {
  const { type, isCreate = false } = props;
  const intl = useIntl();

  return (
    <>
      {type === IdentityProviderType.wechat_qr && <WeChatOAuthConfig isCreate={isCreate} />}
      {type === IdentityProviderType.wechatwork_oauth && (
        <WeChatWorkOAuthConfig isCreate={isCreate} />
      )}
      {type === IdentityProviderType.dingtalk_oauth && <DingTalkOAuthConfig isCreate={isCreate} />}
      {type === IdentityProviderType.qq_oauth && <QqOAuthConfig isCreate={isCreate} />}
      {type === IdentityProviderType.feishu_oauth && <FeiShuOAuthConfig isCreate={isCreate} />}
      {type === IdentityProviderType.gitee_oauth && <GiteeOAuthConfig isCreate={isCreate} />}
      {type === IdentityProviderType.github_oauth && <GithubOAuthConfig isCreate={isCreate} />}
      {type === IdentityProviderType.alipay_oauth && <AliPayOAuthConfig isCreate={isCreate} />}
      <ProFormSwitch
        name={['displayed']}
        extra={intl.formatMessage({
          id: 'pages.authn.identity_provider.config.form.switch.displayed.extra',
        })}
        label={intl.formatMessage({ id: 'app.displayed' })}
      />
    </>
  );
};
export default Config;
