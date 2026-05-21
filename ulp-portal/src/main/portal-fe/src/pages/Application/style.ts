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
import { createStyles } from 'antd-style';

const useStyles = createStyles(({ token, css, prefixCls }, prop) => {
  const prefixClassName = prop as string;
  return css`
    .${prefixClassName} {
      border: none;
      border-radius: ${token.borderRadius};
      .${prefixCls}-pro-table-list-toolbar-container {
      }

      &-item-card {
        .${prefixCls}-card-body {
          padding: 16px !important;
          background: #f7f8fa;
          border-radius: ${token.borderRadius}px;
        }
      }

      &-item-content-wrapper {
        display: flex;
      }

      &-item-avatar {
        width: 54px;
        height: 54px;
        border-radius: 4px;
        margin-right: 5px;

        & .${prefixCls}-avatar {
          width: 54px !important;
          height: 54px !important;
          display: flex;
          align-items: center;
          border-radius: 4px;

          > img {
            height: auto;
          }
        }
      }

      &-item-content {
        display: flex;
        flex-direction: column;
        font-size: 16px;
        min-width: 0;

        &-title {
          font-weight: 500;
          color: #293350;
          margin-top: 4px;
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
        }
        &-type {
          color: #9fabcb;
          font-size: 13px;
          margin-top: 4px;
        }
        &-desc {
          margin-top: 12px;
        }
      }
    }
  `;
});
export default useStyles;
