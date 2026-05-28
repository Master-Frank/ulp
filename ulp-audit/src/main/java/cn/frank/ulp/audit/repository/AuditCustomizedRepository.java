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
package cn.frank.ulp.audit.repository;

import java.time.LocalDateTime;
import java.util.List;

import cn.frank.ulp.audit.event.type.EventType;
import cn.frank.ulp.audit.repository.result.AuditStatisticsResult;
import cn.frank.ulp.audit.repository.result.AuthnQuantityResult;

/**
 * 组织成员
 *
 * @author Frank Zhang
 */
public interface AuditCustomizedRepository {

    List<AuditStatisticsResult> authnHotProvider(List<EventType> types, LocalDateTime startTime,
                                                 LocalDateTime endTime);

    List<AuthnQuantityResult> authnQuantity(List<EventType> types, LocalDateTime startTime,
                                            LocalDateTime endTime, String dateFormat);

    List<AuditStatisticsResult> appVisitRank(EventType type, LocalDateTime startTime,
                                             LocalDateTime endTime);

    List<AuditStatisticsResult> authnZone(List<EventType> types, LocalDateTime startTime,
                                          LocalDateTime endTime);
}
