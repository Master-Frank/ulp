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
package cn.frank.ulp.console.service.analysis;

import java.util.List;

import cn.frank.ulp.audit.repository.result.AuthnQuantityResult;
import cn.frank.ulp.audit.repository.result.AuthnZoneResult;
import cn.frank.ulp.console.pojo.query.analysis.AnalysisQuery;
import cn.frank.ulp.console.pojo.result.analysis.AppVisitRankResult;
import cn.frank.ulp.console.pojo.result.analysis.AuthnHotProviderResult;
import cn.frank.ulp.console.pojo.result.analysis.OverviewResult;

/**
 * 统计 service
 *
 * @author Frank Zhang
 */
public interface AnalysisService {

    /**
     * 概述
     *
     * @return {@link OverviewResult}
     */
    OverviewResult overview();

    /**
     * 认证量统计
     *
     * @param params {@link AnalysisQuery}
     * @return {@link List<AuthnQuantityResult>}
     */
    List<AuthnQuantityResult> authnQuantity(AnalysisQuery params);

    /**
     * 应用热点统计
     *
     * @param params {@link AnalysisQuery}
     * @return {@link List<AppVisitRankResult>}
     */
    List<AppVisitRankResult> appVisitRank(AnalysisQuery params);

    /**
     * 热门认证方式统计
     *
     * @param params {@link AnalysisQuery}
     * @return {@link List<AuthnHotProviderResult>}
     */
    List<AuthnHotProviderResult> authnHotProvider(AnalysisQuery params);

    /**
     * 登录区域统计
     *
     * @param params {@link AnalysisQuery}
     * @return {@link List< AuthnZoneResult >}
     */
    List<AuthnZoneResult> authnZone(AnalysisQuery params);
}
