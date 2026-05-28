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
import { LOGIN_PATH } from '@/utils/utils';
import { history } from '@@/core/history';
import { useIntl, useLocation, useModel } from '@umijs/max';
import { useMount } from 'ahooks';
import { App } from 'antd';
import queryString from 'query-string';

export default () => {
  const { setInitialState } = useModel('@@initialState');
  const location = useLocation();
  const intl = useIntl();
  const { modal } = App.useApp();
  useMount(async () => {
    modal.warning({
      title: intl.formatMessage({ id: 'pages.session-expired.title' }),
      content: intl.formatMessage({ id: 'pages.session-expired.content' }),
      okText: intl.formatMessage({ id: 'pages.session-expired.okText' }),
      okType: 'danger',
      centered: false,
      maskClosable: false,
      okCancel: false,
      onOk: async () => {
        await setInitialState((s: any) => ({ ...s, currentUser: undefined }));
        const query = queryString.parse(location.search);
        const { redirect_uri } = query as { redirect_uri: string };
        let settings: Record<string, string> = { pathname: LOGIN_PATH };
        const domain: string[] | string = redirect_uri && redirect_uri.split('/');
        if (redirect_uri && redirect_uri !== domain[0] + '//' + domain[2] + '/') {
          settings = {
            ...settings,
            search: queryString.stringify({
              redirect_uri: redirect_uri,
            }),
          };
        }
        const href = history.createHref(settings);
        window.location.replace(href);
      },
    });
  });
  return <></>;
};
