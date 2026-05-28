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
package cn.frank.ulp.common.repository.account;

import java.util.ArrayList;
import java.util.List;

import cn.frank.ulp.common.entity.account.UserDetailEntity;

/**
 * User Detail Repository Customized
 *
 * @author Frank Zhang
 */
public interface UserDetailRepositoryCustomized {
    /**
     * 批量新增
     *
     * @param list {@link List}
     */
    void batchSave(List<UserDetailEntity> list);

    /**
     * 批量更新
     *
     * @param list {@link List}
     */
    void batchUpdate(ArrayList<UserDetailEntity> list);
}
