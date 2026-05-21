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
package cn.frank.ulp.identitysource.core.processor;

import java.time.LocalDateTime;
import java.util.List;

import cn.frank.ulp.common.enums.TriggerType;
import cn.frank.ulp.identitysource.core.domain.User;

/**
 * 身份源数据 pull post 处理器
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/3/1 22:04
 */
public interface IdentitySourceSyncUserPostProcessor {
    /**
     * 处理数据
     *
     * @param batch {@link String}
     * @param identitySourceId {@link String}
     * @param userList {@link  List}
     * @param startTime {@link  LocalDateTime}
     * @param triggerType {@link  TriggerType}
     */
    void process(String batch, String identitySourceId, List<User> userList,
                 LocalDateTime startTime, TriggerType triggerType);

}
