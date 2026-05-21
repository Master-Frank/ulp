/*
 * ulp-audit - United Login Platform
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
package cn.frank.ulp.audit.service;

import java.util.List;

import cn.frank.ulp.audit.endpoint.pojo.AuditListQuery;
import cn.frank.ulp.audit.endpoint.pojo.AuditListResult;
import cn.frank.ulp.audit.endpoint.pojo.DictResult;
import cn.frank.ulp.support.repository.page.domain.Page;
import cn.frank.ulp.support.repository.page.domain.PageModel;

/**
 * 审计service
 *
 * @author Frank Zhang
 */
public interface AuditService {
    /**
     * List
     *
     * @param query     {@link AuditListQuery}
     * @param pageModel {@link PageModel}
     * @return {@link Page}
     */
    Page<AuditListResult> getAuditList(AuditListQuery query, PageModel pageModel);

    /**
     * 获取字典类型
     *
     * @param userType {@link String}
     * @return {@link List}
     */
    List<DictResult> getAuditDict(String userType);
}
