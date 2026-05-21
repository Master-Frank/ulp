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
package cn.frank.ulp.common.repository.app;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.frank.ulp.common.entity.app.po.AppAccountPO;
import cn.frank.ulp.common.entity.app.query.AppAccountQueryParam;

/**
 * 应用账户 Repository Customized
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/5/26 23:40
 */
public interface AppAccountRepositoryCustomized {

    /**
     * 获取应用账户列表
     *
     * @param pageable {@link  Pageable}
     * @param query    {@link  AppAccountQueryParam}
     * @return {@link Page}
     */
    Page<AppAccountPO> getAppAccountList(AppAccountQueryParam query, Pageable pageable);
}
