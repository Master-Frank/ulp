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
package cn.frank.ulp.common.repository.identitysource;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import cn.frank.ulp.common.entity.identitysource.IdentitySourceSyncRecordEntity;

/**
 * 身份源同步详情
 *
 * @author Frank Zhang
 */
@Repository
public interface IdentitySourceSyncRecordRepository extends
                                                    JpaRepository<IdentitySourceSyncRecordEntity, String>,
                                                    JpaSpecificationExecutor<IdentitySourceSyncRecordEntity>,
                                                    IdentitySourceSyncRecordRepositoryCustomized {
}
