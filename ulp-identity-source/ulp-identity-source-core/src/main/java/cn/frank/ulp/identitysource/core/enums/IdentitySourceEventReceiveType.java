/*
 * ulp-identity-source-core - United Login Platform
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
package cn.frank.ulp.identitysource.core.enums;

/**
 * 事件通知类型
 *
 * @author TopIAM
 */
public enum IdentitySourceEventReceiveType {
                                            /**
                                             * 用户添加
                                             */
                                            USER_ADD,
                                            /**
                                             * 用户修改
                                             */
                                            USER_MODIFY,
                                            /**
                                             * 用户离职
                                             */
                                            USER_LEAVE,

                                            /**
                                             * 用户删除
                                             */
                                            USER_REMOVE,

                                            /**
                                             * 部门创建
                                             */
                                            DEPT_CREATE,

                                            /**
                                             * 部门修改
                                             */
                                            DEPT_MODIFY,
                                            /**
                                             * 部门删除
                                             */
                                            DEPT_REMOVE
}
