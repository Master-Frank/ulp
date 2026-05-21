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
import { createStyles } from 'antd-style';

const useStyles = createStyles(({ prefixCls, token }) => ({
  expirationDate: {
    [`.${prefixCls}-form-item-control-input-content`]: {
      display: 'flex',
      alignItems: 'center',
    },
    marginBottom: '0 !important',
  },
  passwordLength: {
    marginBottom: ' 0px !important',
  },
  historyPasswordCheck: {
    marginBottom: ' 0px !important',
  },
  historyPasswordCheckCount: {
    padding: '15px !important',
    backgroundColor: token.colorFillAlter,
  },
  passwordComplexity: {
    [`.${prefixCls}-pro-field-radio-vertical  .${prefixCls}-radio-wrapper`]: {
      display: 'flex',
      alignItems: 'center',
      height: '30px',
      marginBottom: ' 5px',
      lineHeight: '30px',
    },
  },
  userScopeList: {
    [`.${prefixCls}-pro-form-list-container`]: {
      width: '100%  !important',
    },
  },
  userListSpace: {
    width: '100%',
    [`.${prefixCls}-space-item:last-child`]: {
      width: '100%',
    },
  },
  userCheckbox: {
    marginBottom: '10px !important',
  },
  excludeCheckboxEnable: {
    marginBottom: '10px !important',
    marginTop: '10px !important',
  },
  excludeCheckboxDisable: {
    marginTop: '10px !important',
  },
  excludeUserItem: {
    marginBottom: '0 !important',
  },
}));

export default useStyles;
