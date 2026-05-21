/*
 * ulp-common - United Login Platform
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
package cn.frank.ulp.common.enums;

/**
 * 检查有效性类型
 *
 * @author Frank Zhang
 */
public enum CheckValidityType {
                               /**
                                * 邮箱
                                */
                               EMAIL,
                               /**
                                * 手机号
                                */
                               PHONE,
                               /**
                                * 名称
                                */
                               NAME,
                               /**
                                * 编码
                                */
                               CODE,
                               /**
                                * 用户名
                                */
                               USERNAME;
}
