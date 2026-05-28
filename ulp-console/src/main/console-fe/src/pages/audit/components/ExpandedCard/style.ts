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

const useStyles = createStyles(({ token, prefixCls }) => ({
  expandedCard: {
    display: 'flex',
    paddingLeft: '10px',
    [`.${prefixCls}-table-expanded-row-fixed`]: {
      width: 'auto !important',
    },
  },
  expandedCardEachLine: {
    display: 'flex',
    marginBottom: '14px',
  },
  expandedCardLineUser: {
    flex: 1,
    marginRight: '24px',
  },
  expandedCardLineEvent: {
    flex: 1,
    marginRight: '24px',
  },
  expandedCardLineGeo: {
    flex: 1,
    marginRight: '24px',
  },
  expandedCardLabel: {
    display: 'block',
    minWidth: '80px',
    color: token.colorTextLabel,
    fontSize: '14px',
    [`.${prefixCls}-collapse-header`]: {
      paddingTop: '0 !important',
      paddingBottom: '0 !important',
    },
    [`.${prefixCls}-collapse-header-text`]: {
      color: token.colorTextLabel,
    },
  },
  expandedCardContent: {
    display: 'block',
    flexWrap: 'wrap',
    overflowX: 'auto',
    color: token.colorText,
    fontSize: '14px',
  },
  expandedCardTarget: {
    flex: 1,
  },
  expandedCardPanel: {
    fontSize: '14px',
    [`.${prefixCls}-collapse-header`]: {
      paddingTop: '0 !important',
      paddingBottom: '0 !important',
    },
    [`.${prefixCls}-collapse-header-text`]: {
      color: token.colorTextLabel,
    },
    [`.${prefixCls}-collapse-content>.${prefixCls}-collapse-content-box`]: {
      paddingTop: '6px  !important',
      paddingBottom: '6px  !important',
    },
  },
  expandedCardPanelContent: {
    padding: '0px 24px  !important',
    [`p`]: {
      marginTop: '0px !important',
      marginBottom: '6px !important',
    },
  },
}));

export default useStyles;
