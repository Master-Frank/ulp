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
package cn.frank.ulp.identitysource.core.event;

/**
     * 身份源配置事件类型
     *
     * @author TopIAM
     * Created by support@topiam.cn on 2022/3/20 21:59
     */
public enum IdentitySourceEventType {
                                     /**
                                      * 注册
                                      */
                                     REGISTER,
                                     /**
                                      * 销毁
                                      */
                                     DESTROY,

                                     /**
                                      * 同步
                                      */
                                     SYNC
}